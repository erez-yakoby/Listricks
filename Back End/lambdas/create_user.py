import json
import boto3

dynamodb_client = boto3.resource('dynamodb')
table = dynamodb_client.Table('listricks-user-data')


def lambda_handler(event, context):
    try:
        body = json.loads(event["body"])
        username = body['userId']

        response = table.get_item(
            Key={
                'userId': username
            }
        )
        if 'Item' not in response:
            table.put_item(Item=body)
            return {
                "statusCode": 201,
                "headers": {
                    "Content-Type": "application/json"
                },
                "body": json.dumps({
                    "message": "User Created"
                })
            }

        else:
            return {
                "statusCode": 400,
                "headers": {
                    "Content-Type": "application/json"
                },
                "body": json.dumps({
                    "message": "user name already exist"
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