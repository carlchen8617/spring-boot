import os
import boto3

# Assume Role and return Boto3 Session as that role
def role_arn_to_session(**args):
    """
    Usage :
        session = role_arn_to_session(
            RoleArn='arn:aws:iam::012345678901:role/example-role',
            RoleSessionName='ExampleSessionName')
        client = session.client('sqs')
    """
    client = boto3.client('sts')
    response = client.assume_role(**args)
    return boto3.Session(
        aws_access_key_id=response['Credentials']['AccessKeyId'],
        aws_secret_access_key=response['Credentials']['SecretAccessKey'],
        aws_session_token=response['Credentials']['SessionToken'])
  
def get_user_info(pnum, dlr_table_name, ddb_res):
    resp = ddb_res.meta.client.get_item(TableName=dlr_table_name, Key={'p_number': pnum})
    return resp.get('Item', None)
  
def add_update_delete_team(ddb_res, account_name):
    pnums = [x.strip().lower() for x in os.getenv("P-Number").split(',')]
    env = os.getenv("Environment").title()
    teams = os.getenv("TeamCode")
    print("Teams: {}".format(teams))
    item_type = os.getenv("Type")
    
    dlr_table_name = 'NDC{}DLR-{}'.format(env, env)
    
    for pnum in pnums:
        user_info = get_user_info(pnum, dlr_table_name, ddb_res)
        
        if user_info:
            cur_teams = user_info.get('team_key', [])
            altered_teams = []
            
            if not isinstance(cur_teams, list):
                cur_teams = []
            
            if 'Default' in cur_teams:
                cur_teams.remove('Default')
                
            if 'NONE' in cur_teams:
                cur_teams.remove('NONE')
                
            if item_type == 'DELETE':
                for item in teams.split(','):
                    try:
                        cur_teams.remove(item)
                        altered_teams.append(item)
                    except:
                        print('{} is not a part of {} lab'.format(pnum, item))
                        
                if not cur_teams:
                    cur_teams = "Default"
            elif item_type == 'ADD':
                for item in teams.split(','):
                    if item in cur_teams:
                        print("User already exists within team: {}".format(item))
                    else:    
            	    
                        cur_teams.append(item)
                        altered_teams.append(item)
            	        
            ddb_res.meta.client.update_item(
                TableName=dlr_table_name,
                Key={'p_number': pnum},
                UpdateExpression="SET team_key = :t",
                ExpressionAttributeValues={':t': cur_teams }
            )
            operation = 'added to' if item_type == 'ADD' else 'deleted from'
            print(" {} successfully {} user {} in {}".format(altered_teams, operation, pnum, account_name))
        elif item_type == 'DELETE':
            print(" User {} doesn't exist in {}!".format(pnum, account_name))
        else:
            ddb_res.meta.client.put_item(
                TableName=dlr_table_name,
                Item={
                    'p_number': pnum,
                    'team_key': teams.split(','),
                    'model_key': ['NONE'],
                    'redshift_temp_pass': b'ERROR',
                    'enabled': True
                }
            )
            print(" Successfully created user {} with teams {} in {}".format(pnum, teams, account_name))

# Configure resource for LZ198 dynamodb
local_session = boto3.session.Session()
local_resource = local_session.resource('dynamodb', 'ap-southeast-2')

# Configure resource for ndc dynambodb
remote_account_id = '435177180026'
if os.getenv("Environment").title() in ('Dev', 'Test'):
    remote_account_id = '191824718585'
    
remote_session = role_arn_to_session(
    RoleArn='arn:aws:iam::{}:role/HIPProvisioningInstanceProfile'.format(remote_account_id),
    RoleSessionName='LZ198P')
remote_resource = remote_session.resource('dynamodb', 'ap-southeast-2')

add_update_delete_team(local_resource, 'lz198')

