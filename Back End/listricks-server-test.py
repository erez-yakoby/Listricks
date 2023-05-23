import pytest
import requests

PATH = "https://esy2ay1vg5.execute-api.us-west-2.amazonaws.com/prod/"

CREAT_NEW_USER = {"userId": "user1",
                  "email": "user1",
                  "listsId": [],
                  "password": "111"}

ADD_LIST_JSON = {"listId": "list1",
                 "products": {},
                 "usersAccess": ['user1']}

ADD_PRODUCT_JSON = {"productName": "prod1",
                    "isMarked": False}


@pytest.fixture(autouse=True)
def run_before_tests():
    requests.delete(PATH + "users/user1/lists/list1")
    requests.delete(PATH + "tests/user1")
    yield


def test_create_user_success():
    r = requests.post(PATH + 'users', json=CREAT_NEW_USER)
    assert (r.status_code == 201)
    assert (r.json() == {"message": "User Created"})


def test_create_existing_user():
    requests.post(PATH + 'users', json=CREAT_NEW_USER)
    r = requests.post(PATH + 'users', json=CREAT_NEW_USER)
    assert (r.status_code == 400)
    assert (r.json() == {"message": "user name already exist"})


def test_login_user_success():
    requests.post(PATH + 'users', json=CREAT_NEW_USER)
    r = requests.get(PATH + 'users/login',
                     params={'username': 'user1', 'password': '111'})
    assert (r.status_code == 200)
    assert (r.json() == {"message": "Login Succeeded"})


def test_login_user_username_not_exist():
    requests.post(PATH + 'users', json=CREAT_NEW_USER)
    r = requests.get(PATH + 'users/login',
                     params={'username': 'user2', 'password': '111'})
    assert (r.status_code == 400)
    assert (r.json() == {"message": "Wrong username/password supplied"})


def test_login_user_wrong_password():
    requests.post(PATH + 'users', json=CREAT_NEW_USER)
    r = requests.get(PATH + 'users/login',
                     params={'username': 'user1', 'password': '222'})
    assert (r.status_code == 400)
    assert (r.json() == {"message": "Wrong username/password supplied"})


def test_add_list_success():
    requests.post(PATH + 'users', json=CREAT_NEW_USER)
    r = requests.put(PATH + 'users/user1/lists', json=ADD_LIST_JSON)
    assert (r.status_code == 200)
    assert (r.json() == {'lists': ['list1']})


def test_add_existing_list():
    requests.post(PATH + 'users', json=CREAT_NEW_USER)
    requests.put(PATH + 'users/user1/lists', json=ADD_LIST_JSON)
    r = requests.put(PATH + 'users/user1/lists', json=ADD_LIST_JSON)
    assert (r.status_code == 400)
    assert (r.json() == {"message": "list name already exist"})


def test_get_list_success():
    requests.post(PATH + 'users', json=CREAT_NEW_USER)
    requests.put(PATH + 'users/user1/lists', json=ADD_LIST_JSON)
    r = requests.get(PATH + 'users/user1/lists')
    assert (r.status_code == 200)
    assert (r.json() == {'lists': ['list1']})


def test_delete_list_success():
    requests.post(PATH + 'users', json=CREAT_NEW_USER)
    requests.put(PATH + 'users/user1/lists', json=ADD_LIST_JSON)
    r = requests.delete(PATH + 'users/user1/lists/list1')
    assert (r.status_code == 200)
    assert (r.json() == {'lists': []})


def test_add_product_success():
    requests.post(PATH + 'users', json=CREAT_NEW_USER)
    requests.put(PATH + 'users/user1/lists', json=ADD_LIST_JSON)
    r = requests.put(PATH + 'lists/list1/products', json=ADD_PRODUCT_JSON)
    assert (r.status_code == 200)
    assert (r.json() == {'products': {'prod1': False}})


def test_delete_product_success():
    requests.post(PATH + 'users', json=CREAT_NEW_USER)
    requests.put(PATH + 'users/user1/lists', json=ADD_LIST_JSON)
    requests.put(PATH + 'lists/list1/products', json=ADD_PRODUCT_JSON)
    r = requests.delete(PATH + 'lists/list1/products/prod1')
    assert (r.status_code == 200)
    assert (r.json() == {'products': {}})


def test_select_product_success_false_to_true():
    requests.post(PATH + 'users', json=CREAT_NEW_USER)
    requests.put(PATH + 'users/user1/lists', json=ADD_LIST_JSON)
    requests.put(PATH + 'lists/list1/products', json=ADD_PRODUCT_JSON)
    r = requests.put(PATH + 'lists/list1/products/prod1')
    assert (r.status_code == 200)
    assert (r.json() == {'products': {'prod1': True}})


def test_select_product_success_true_to_false():
    requests.post(PATH + 'users', json=CREAT_NEW_USER)
    requests.put(PATH + 'users/user1/lists', json=ADD_LIST_JSON)
    requests.put(PATH + 'lists/list1/products', json=ADD_PRODUCT_JSON)
    requests.put(PATH + 'lists/list1/products/prod1')
    r = requests.put(PATH + 'lists/list1/products/prod1')
    assert (r.status_code == 200)
    assert (r.json() == {'products': {'prod1': False}})









