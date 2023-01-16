import unittest
import Function_for_uni_sub_distances as fusd


class TestDataProcessing(unittest.TestCase):
    def test_for_distance(self):
        minimum = 105.35 * (1 - 0.005)
        maximum = 105.35 * (1 + 0.005)
        assert minimum <= fusd.data.iloc[0, -1] <= maximum, 'Wrong distance'
        
    def test_for_rows(self):
        self.assertEqual(len(fusd.uni) * len(fusd.sub), len(fusd.data), 'Wrong number of rows')
        
    def test_for_null(self):
        self.assertEqual(fusd.data.isnull().sum().all(), 0, 'There are null values')


# Driver code
# -----------
unittest.main(verbosity=2)