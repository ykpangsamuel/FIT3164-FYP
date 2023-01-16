import pandas as pd


file = pd.ExcelFile('C:/Users/User/Desktop/Moving_annual_rents_by_suburb_December_quarter_2021.xlsx')
data = pd.read_excel(file, 'All properties')

data.to_csv('rental_data.csv', index=False)
