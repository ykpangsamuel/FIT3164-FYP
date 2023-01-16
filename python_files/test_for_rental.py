import unittest
import Function_for_Rental as fr


class TestDataProcessing(unittest.TestCase):
    def test_for_data_type_1(self):
        self.assertEqual(fr.details['rental'].dtype, int, 'Wrong data type')
        
    def test_for_data_type_2(self):
        self.assertEqual(fr.vis.iloc[:, -1].dtype, float, 'Wrong data type')
        
    def test_for_clean_suburbs_1(self):
        self.assertNotIn('Newcombe', fr.details['suburb'], 'Old value is still present')
        
    def test_for_clean_suburbs_2(self):
        self.assertNotIn('Bendigo East', fr.vis['suburb'], 'Old value is still present')
        
    def test_for_nulls_1(self):
        self.assertEqual(fr.details.isnull().sum().all(), 0, 'There are null values')
        
    def test_for_nulls_2(self):
        self.assertEqual(fr.vis.isnull().sum().all(), 0, 'There are null values')

# Driver code
# -----------
unittest.main(verbosity=2)



