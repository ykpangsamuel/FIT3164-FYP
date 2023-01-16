import pandas as pd


file = pd.ExcelFile('C:/Users/User/Desktop/Data_Tables_LGA_Recorded_Offences_Year_Ending_December_2021.xlsx')
data = pd.read_excel(file, 'Table 03')

# data.to_csv('crime_data.csv', index=False)
