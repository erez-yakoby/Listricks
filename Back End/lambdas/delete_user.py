import json
import boto3

dynamodb = boto3.resource('dynamodb')
users_table = dynamodb.Table('listricks-user-data')


def lambda_handler(event, context):
    user_id = event['pathParameters']['username']

    try:
        response = users_table.delete_item(
            Key={'userId': user_id}
        )
        return {
            'statusCode': 200,
            "headers": {
                "Content-Type": "application/json"
            },
            'body': json.dumps({
                'message': "user deleted"
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
