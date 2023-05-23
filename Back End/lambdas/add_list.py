import json
import boto3

dynamodb_client = boto3.resource('dynamodb')
users_table = dynamodb_client.Table('listricks-user-data')
lists_table = dynamodb_client.Table('listricks-lists')


def lambda_handler(event, context):
    try:
        username = event['pathParameters']['username']
        body = json.loads(event["body"])
        listname = body['listId']

        response = lists_table.get_item(
            Key={
                'listId': listname
            }
        )
        if 'Item' not in response:
            lists_table.put_item(Item=body)

        else:
            return {
                "statusCode": 400,
                "headers": {
                    "Content-Type": "application/json"
                },
                "body": json.dumps({
                    "message": "list name already exist"
                })
            }

        # update the listsId attribute for the specific user
        response = users_table.update_item(
            Key={'userId': username},
            UpdateExpression='SET listsId = list_append(listsId, :new_item)',
            ExpressionAttributeValues={
                ':new_item': [listname],
            },
            ReturnValues="UPDATED_NEW"
        )
        new_listsId = response["Attributes"]["listsId"]

        return {
            "statusCode": 200,
            "headers": {
                "Content-Type": "application/json"
            },
            "body": json.dumps({
                "lists": new_listsId

            })
        }

    except:
        return {
            "statusCode": 500,
            "headers": {
                "Content-Type": "application/json"
            },
            "body": json.dumps({
                "message": "Internal Server Errort"
            })
        }







