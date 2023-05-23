import json
import boto3

dynamodb_client = boto3.resource('dynamodb')
users_table = dynamodb_client.Table('listricks-user-data')
lists_table = dynamodb_client.Table('listricks-lists')


def lambda_handler(event, context):
    try:
        body = json.loads(event["body"])

        listname = event['pathParameters']['listName']
        username = body

        # update the listsId list of the user
        response = users_table.update_item(
            Key={'userId': username},
            UpdateExpression='SET listsId = list_append(listsId, :new_item)',
            ExpressionAttributeValues={
                ':new_item': [listname],
            },
        )

        # update the usersAccess list of the list
        response = lists_table.update_item(
            Key={'listId': listname},
            UpdateExpression='SET usersAccess = list_append(usersAccess, :new_item)',
            ExpressionAttributeValues={
                ':new_item': [username],
            },
        )

        return {
            "statusCode": 200,
            "headers": {
                "Content-Type": "application/json"
            },
            "body": json.dumps({
                "message": "List shared successfully"

            })
        }

    except:
        return {
            "statusCode": 500,
            "headers": {
                "Content-Type": "application/json"
            },
            "body": json.dumps({
                "message": "Internal Server Error"
            })
        }









