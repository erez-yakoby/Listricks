import json
import boto3

dynamodb_client = boto3.resource('dynamodb')
table = dynamodb_client.Table('listricks-lists')


def lambda_handler(event, context):
    try:
        listName = event["pathParameters"]["listName"]

        products_query = table.get_item(
            TableName='listricks-lists',
            Key={"listId": listName},
            AttributesToGet=['products'])["Item"]["products"]

        return {
            "statusCode": 200,
            "headers": {
                "Content-Type": "application/json"
            },
            "body": json.dumps({
                "products": products_query

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
