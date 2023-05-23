import json
import boto3

dynamodb_client = boto3.resource('dynamodb')
lists_table = dynamodb_client.Table('listricks-lists')


def lambda_handler(event, context):
    try:
        body = json.loads(event["body"])
        listname = event['pathParameters']['listName']
        product_name = body['productName']
        is_marked = body['isMarked']

        response = lists_table.update_item(
            Key={'listId': listname},
            UpdateExpression=f"SET products.#product_name = :is_marked",
            ExpressionAttributeNames={
                "#product_name": product_name
            },
            ExpressionAttributeValues={
                ':is_marked': is_marked,
            },
            ReturnValues='UPDATED_NEW',
        )

        products = response["Attributes"]["products"]

        return {
            "statusCode": 200,
            "headers": {
                "Content-Type": "application/json"
            },
            "body": json.dumps({
                "products": products

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








