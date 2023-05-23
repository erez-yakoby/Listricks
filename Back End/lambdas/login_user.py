import json
import boto3

dynamodb_client = boto3.resource('dynamodb')
table = dynamodb_client.Table('listricks-user-data')


def lambda_handler(event, context):
    try:
        userName = event["queryStringParameters"]["username"]
        password = event["queryStringParameters"]["password"]

        password_query = table.get_item(
            TableName='listricks-user-data',
            Key={"userId": userName},
            AttributesToGet=['password'])["Item"]["password"]

        if password == password_query:
            return {
                "statusCode": 200,
                "headers": {
                    "Content-Type": "application/json"
                },
                "body": json.dumps({
                    "message": "Login Succeeded"
                })
            }

        else:
            raise KeyError


    except KeyError:
        return {
            "statusCode": 400,
            "headers": {
                "Content-Type": "application/json"
            },
            "body": json.dumps({
                "message": "Wrong username/password supplied"
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