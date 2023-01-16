import pandas as pd

data = pd.read_csv(f'C:/Users/User/Desktop/Universities_with_coordinates.csv')

# data.dropna(axis=0, how='all', inplace=True)
# data.dropna(axis=1, how='all', inplace=True)
data.rename(columns={'description':'campus'}, inplace=True)

data['campus'] = ',' + data['campus']
data['campus'].fillna('', inplace=True)
data['name'] = data['name'] + data['campus']
del data['campus']

data['WKT'] = data['WKT'].str[7:-1]
temp = data['WKT'].str.split(expand=True)
data['latitude']  = temp[1]
data['longitude'] = temp[0]
del data['WKT']

data.to_csv('university_list.csv', index=False)

