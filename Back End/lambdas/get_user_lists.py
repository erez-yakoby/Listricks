import json
import boto3

dynamodb_client = boto3.resource('dynamodb')
table = dynamodb_client.Table('listricks-user-data')


def lambda_handler(event, context):
    try:
        userName = event["pathParameters"]["username"]
        lists_query = table.get_item(
            TableName='listricks-user-data',
            Key={"userId": userName},
            AttributesToGet=['listsId'])["Item"]["listsId"]

        return {
            "statusCode": 200,
            "headers": {
                "Content-Type": "application/json"
            },
            "body": json.dumps({
                "lists": lists_query

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