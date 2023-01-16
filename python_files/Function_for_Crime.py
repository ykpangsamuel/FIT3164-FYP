import pandas as pd


# s_lga = pd.read_csv('C:/Users/User/Desktop/suburb_by_lga.csv', header=None)

# s_lga.columns = ['LGA', 'Suburb']
# s_lga = s_lga.dropna()
# s_lga['Suburb'] = s_lga['Suburb'].str.replace('.', '', regex=False)
# s_lga['Suburb'] = s_lga['Suburb'].str.replace(' and', ',', regex=False)
# s_lga['Suburb'] = s_lga['Suburb'].str.split(', ')
# s_lga = s_lga.explode('Suburb').reset_index(drop=True)

# crime = crime[[crime.columns[i] for i in [0, 3, 5, 9]]]
# crime.iloc[:, -1] = crime.iloc[:, -1].str.replace(',', '')
# crime.iloc[:, -1] = crime.iloc[:, -1].astype(float)
# crime = crime.groupby(['Year', 'Local Government Area']).sum().reset_index()


def clean_crime(data):
    data = data[[data.columns[i] for i in (0, 4, 8)]]
    data = data.groupby(list(data.columns[:-1])).sum().reset_index()
    data.iloc[:, 1] = data.iloc[:, 1].str.replace(' Central', '')
    return data
    
def add_mean(data):
    temp = data.groupby([data.columns[0]]).mean().reset_index()
    temp.insert(1, data.columns[1], ['average'] * len(temp))
    temp = pd.concat([data, temp])
    return temp


# crime = pd.read_excel('C:/Users/User/Desktop/Data_Tables_LGA_Recorded_Offences_Year_Ending_December_2021.xlsx')
# crime.to_csv('Data_Tables_LGA_Recorded_Offences_Year_Ending_December_2021.csv', index=False)


crime = pd.read_csv('C:/Users/User/Desktop/crime_data.csv')

crime = clean_crime(crime)
crime = add_mean(crime)
crime.to_csv('crime_rate.csv', index=False)
