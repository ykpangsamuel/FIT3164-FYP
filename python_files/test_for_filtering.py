import unittest
import Function_for_Synthetic_Data as frsd


class TestDataProcessing(unittest.TestCase):
    def test_for_columns(self):
        self.assertEqual(len(frsd.data.columns), 9, 'Wrong number of columns')
    
    def test_for_top_K_suburbs(self):
        self.assertEqual(frsd.data['answer'].str.len().unique(), frsd.top_K, 'Wrong number of recommendations')
        
    def test_for_null(self):
        self.assertEqual(frsd.data.isnull().sum().all(), 0, 'There are null values')


# Driver code
# -----------
unittest.main(verbosity=2)



