import json
import boto3

dynamodb = boto3.resource('dynamodb')
lists_table = dynamodb.Table('listricks-lists')


def lambda_handler(event, context):
    try:
        listname = event['pathParameters']['listName']
        product_name = event['pathParameters']['productName']

        # Get the current value of the is_marked flag for the product
        response = lists_table.get_item(
            Key={'listId': listname},
            ProjectionExpression=f"products.#product_name",
            ExpressionAttributeNames={
                '#product_name': product_name,
            },
        )

        is_marked = response['Item']['products'][product_name]

        is_marked = not is_marked

        # Update the item in the database with the new value of the is_marked flag
        response = lists_table.update_item(
            Key={'listId': listname},
            UpdateExpression=f"SET products.#product_name = :is_marked",
            ExpressionAttributeNames={
                '#product_name': product_name,
            },
            ExpressionAttributeValues={
                ':is_marked': is_marked,
            },
            ReturnValues='UPDATED_NEW',
        )

        products = response['Attributes']['products']
        return {
            'statusCode': 200,
            "headers": {
                "Content-Type": "application/json"
            },
            'body': json.dumps({
                'products': products
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
