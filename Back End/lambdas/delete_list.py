import boto3
import json

dynamodb = boto3.resource('dynamodb')
users_table = dynamodb.Table('listricks-user-data')
lists_table = dynamodb.Table('listricks-lists')


def lambda_handler(event, context):
    try:
        username = event['pathParameters']['username']
        listname = event['pathParameters']['listName']

        response = users_table.get_item(
            Key={
                'userId': username
            }
        )
        list_id = response['Item']['listsId']

        if listname in list_id:
            list_id.remove(listname)
            response = users_table.update_item(
                Key={
                    'userId': username
                },
                UpdateExpression='SET listsId = :val1',
                ExpressionAttributeValues={
                    ':val1': list_id
                },
                ReturnValues='UPDATED_NEW'
            )
        list_id = response['Attributes']['listsId']

        response = lists_table.get_item(
            Key={
                'listId': listname
            }
        )
        users_access = response['Item']['usersAccess']
        users_access.remove(username)
        if users_access == []:
            response = lists_table.delete_item(
                Key={
                    'listId': listname
                },
            )
        else:
            response = lists_table.update_item(
                Key={
                    'listId': listname
                },
                UpdateExpression='SET usersAccess = :val1',
                ExpressionAttributeValues={
                    ':val1': users_access
                },
            )

        return {
            'statusCode': 200,
            "headers": {
                "Content-Type": "application/json"
            },
            'body': json.dumps({
                'lists': list_id
            })
        }

    except:
        return {
            'statusCode': 500,
            "headers": {
                "Content-Type": "application/json"
            },
            'body': json.dumps({
                'message': "Internal Server Error"
            })
        }

