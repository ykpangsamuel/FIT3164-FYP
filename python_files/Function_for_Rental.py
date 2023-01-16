import pandas as pd


def process_rental(data):
    data.iloc[0, :].fillna(method='ffill', inplace=True)
    data = data.loc[:, ~data.iloc[1, :].isin(['Count'])]
    data = data.loc[ ~data.iloc[:, 1].isin(['Group Total']), :]
    data.drop(1, axis=0, inplace=True)
    data.iloc[0, 2:] = data.iloc[0, 2:].str[4:].astype('int')
    data.reset_index(drop=True, inplace=True)
    data.iloc[:, 2:] = data.iloc[:, 2:].replace('-', 0,)
    return data


def buffer_data(data):
    temp = data.iloc[:, 2:]
    temp = temp.replace('-', 0)
    return temp


# For details
# -----------
def data_details(data, temp):
    temp_details = pd.concat([data.iloc[:, :2], temp.iloc[:, -1]], axis=1)
    temp_details = temp_details.iloc[1:, 1:]
    temp_details.columns = ['suburb', 'rental']
    temp_details['rental'] = temp_details['rental'].astype(int)
    return temp_details


# For visualisation
# -----------------
def data_vis(data, temp):
    years = pd.unique(data.iloc[0, :])[1:]

    for i in years:
        temp1 = temp.loc[:, temp.iloc[0, :] == i]
        temp2 = temp1.iloc[0:, :].mean(axis=1)
        temp.drop(temp.columns[temp.iloc[0, :] == i], axis=1, inplace=True)
        temp[temp1.columns[0]] = temp2

    temp.iloc[0:1, :] = temp.iloc[0:1, :].astype(int).astype(str)
    temp_vis = pd.concat([data.iloc[:, :2], temp], axis=1)
    temp_vis.iloc[6:10, 1:5] = temp_vis.iloc[6:10, 1:5].replace(0, float('nan'))
    temp_vis.iloc[6:10, 1:5] = temp_vis.iloc[6:10, 1:5].fillna(method='ffill', axis=1)
    temp_vis = temp_vis.iloc[1:, 1:]
    temp_vis.columns = ['suburb'] + list(years)
    temp_vis.iloc[:, 1:] = temp_vis.iloc[:, 1:].astype(float)
    return temp_vis


# Suburb changes
# --------------
# remove 'St Kilda Rd'
# remove 'Yarra Ranges'
# change 'West St Kilda'  to 'St Kilda West'
# change 'East St Kilda'  to 'St Kilda East'
# change 'East Brunswick' to 'Brunswick East'
# change 'West Brunswick' to 'Brunswick West'
# change 'Mt Martha'      to 'Mount Martha'
# change 'Newcombe'       to 'Newcomb'
# change 'Wanagaratta'    to 'Wangaratta'
# change 'Bendigo East'   to 'East Bendigo'
# change 'East Hawthorn'  to 'Hawthorn East'
# change 'Mt Eliza'       to 'Mount Eliza'


remove_suburbs = ('St Kilda Rd', 'Yarra Ranges')

rename_suburbs = {'West St Kilda' : 'St Kilda West',
                  'East St Kilda' : 'St Kilda East',
                  'East Brunswick': 'Brunswick East',
                  'West Brunswick': 'Brunswick West',
                  'Mt Martha'     : 'Mount Martha',
                  'Newcombe'      : 'Newcomb',
                  'Wanagaratta'   : 'Wangaratta',
                  'Bendigo East'  : 'East Bendigo',
                  'East Hawthorn' : 'Hawthorn East',
                  'Mt Eliza'      : 'Mount Eliza'}

def clean_suburbs(data, remove_suburbs, rename_suburbs):
    data = data.replace({'suburb': rename_suburbs})
    data = data.loc[~ data['suburb'].isin(remove_suburbs)]
    return data


# Driver code
# -----------
data = pd.read_excel('C:/Users/User/Desktop/Moving_annual_rents_Partially_Cleaned.xlsx')

data = process_rental(data)
temp = buffer_data(data)

details = data_details(data, temp)
details = clean_suburbs(details, remove_suburbs, rename_suburbs)

vis     = data_vis(data, temp)
vis     = clean_suburbs(vis, remove_suburbs, rename_suburbs)

# details.to_csv('rental_rate_for_details.csv', index=False)
# vis.to_csv('rental_rate_for_vis.csv', index=False)
