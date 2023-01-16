import unittest
import Function_for_Crime as fc


class TestDataProcessing(unittest.TestCase):
    def test_for_columns(self):
        self.assertEqual(len(fc.crime.columns), 3, 'Wrong number of columns')
    
    def test_for_mean(self):
        self.assertIn('average', fc.crime.iloc[:, 1].values, 'There is no mean value')
        
    def test_for_null(self):
        self.assertEqual(fc.crime.isnull().sum().all(), 0, 'There are null values')

# Driver code
# -----------
unittest.main(verbosity=2)