import csv, numpy
from scipy import stats
from collections import defaultdict

"""
Generates a dictionary template for the 45 features meant to be extracted from the data
Parameters:
    attr_list - List of the sensory attribute names
Returns:
    attr_dict - dictionary containing keys corresponding to desire features
"""
def generateAttributeDict(attr_list):
    if 'RTC time' in attr_list:
        attr_list.remove('RTC time')
    attr_dict = defaultdict(lambda:'')
    sensor_list = ['Acc', 'Gyro']
    correlation_list = []#['_XY', '_XZ', '_YZ']
    axes = ['X', 'Y', 'Z']
    for sensor in sensor_list:
        for correlation in correlation_list:
            attr_dict[sensor+correlation] = ''
        for axis in axes:
            attr_dict[sensor+axis+"_first"] = ''
            attr_dict[sensor+axis+"_second"] = ''
            #attr_dict[sensor+axis+"_third"] = ''
            #attr_dict[sensor+axis+"_fourth"] = ''
    return attr_dict

"""
Most of the statistical operations occur within this class
"""
class feature_extractor:
    """
    When constructed, the object converts all data from instances
    to a numpy array format and stores it in self.inst_list.
    Feature_dict will eventually contain the extracted feature template
    final_instance_list will contain all feature_dicts eventually
    Parameters:
        inst_list - list of instances that are of the following format:
            list containing dictionaries with attributes mapped to lists of values
            ex: [ {key:[data], key2:[data2]} ]
    """
    def __init__(self, inst_list, attr_list):
        self.inst_list = inst_list
        self.attr_list = attr_list
        for i, subdict in enumerate(self.inst_list):
            for key, val in subdict.iteritems():
                if key != 'RTC time' and key != 'label':
                    subdict[key] = numpy.array(val)
            self.inst_list[i] = subdict
        self.feature_dict = None
        self.final_instance_list = []

    """
    Loops through dictionaries representing instances and calls needed
    statistical methods to generate the correct features. All updates occur
    in self.feature_dict. Each updated instance with the correct features is
    added to the self.final_instance_list.
    """
    def extractFeatures(self):
        for i, subdict in enumerate(self.inst_list):
            self.feature_dict = generateAttributeDict(self.attr_list)
            self.determine_moments(subdict)
        #    self.determine_correlations(subdict)
            self.feature_dict['label'] = subdict['label']
            self.final_instance_list.append(self.feature_dict)

    """
    Will write final_instance_list containing feature_dicts of all instances to a CSV
    Parameters:
        filename - name of CSV file to write to
    """
    def writeToCSV(self, filename):
        fieldnames = [key for key in self.feature_dict.keys() if key != 'label']
        fieldnames.append('label')

        with open(filename, 'w') as fh:
            writer = csv.DictWriter(fh, fieldnames=fieldnames)
            writer.writeheader()
            for instance in self.final_instance_list:
                writer.writerow(instance)

    """
    Update feature_dict to include moments 1&2 for each relevant attribute
    Parameter:
        subdict - current instance dictionary that data is pulled from
    """
    def determine_moments(self, subdict):
        for key in subdict.keys():
            if key == 'RTC time' or key == 'label':
                continue
            #if key is not 'RTC time' and key is not 'label':
            self.feature_dict[key+"_first"] = numpy.mean(subdict[key])
            self.feature_dict[key+"_second"] = numpy.var(subdict[key])
            #self.feature_dict[key+"_third"] = stats.skew(subdict[key])
            #self.feature_dict[key+"_fourth"] = stats.kurtosis(subdict[key])

    """
    Update feature_dict to include correlations between the x,y,z of Acc, Gyro,
    and Mag.
    Parameter:
        subdict - current instance dictionary that data is pulled from
    """
    def determine_correlations(self, subdict):
        sensor_list = ['Acc', 'Gyro', 'Mag']
        for sensor in sensor_list:
            if not numpy.any(subdict[sensor+'X']):
                continue
            sensorX = subdict[sensor+'X']
            sensorY = subdict[sensor+'Y']
            sensorZ = subdict[sensor+'Z']
            a, b = self.matchShapes(sensorX, sensorY)
            self.feature_dict[sensor+"_XY"] = stats.pearsonr(a, b)
            a, b = self.matchShapes(sensorY, sensorZ)
            self.feature_dict[sensor+"_YZ"] = stats.pearsonr(a, b)
            a, b = self.matchShapes(sensorX, sensorZ)
            self.feature_dict[sensor+"_XZ"] = stats.pearsonr(a, b)

    """
    Makes sure that the shapes of the arrays for the correlation coefficient
    match.
    Parameters:
        a - first numpy.array
        b - second numpy.array
    """
    def matchShapes(self, a, b):
        shortest_len = min(a.shape[0], b.shape[0])
        a = a[:shortest_len]
        b = b[:shortest_len]
        return (a,b)
