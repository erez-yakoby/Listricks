import boto3
import json

dynamodb = boto3.resource('dynamodb')
lists_table = dynamodb.Table('listricks-lists')


def lambda_handler(event, context):
    try:
        listname = event['pathParameters']['listName']
        productname = event['pathParameters']['productName']

        response = lists_table.get_item(
            Key={
                'listId': listname
            }
        )
        products = response['Item']['products']

        if productname in products:
            products.pop(productname)

            response = lists_table.update_item(
                Key={
                    'listId': listname
                },
                UpdateExpression='SET products = :val1',
                ExpressionAttributeValues={
                    ':val1': products
                },
                ReturnValues='UPDATED_NEW'

            )
        new_products = response['Attributes']['products']

        return {
            'statusCode': 200,
            "headers": {
                "Content-Type": "application/json"
            },
            'body': json.dumps({
                'products': new_products
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

