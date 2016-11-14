import FeatureExtraction as fe
import csv
from collections import defaultdict

"""
Global script below pulls current sensory data from CSV tricks.csv
and stores data in a list containing dictionaries that represent each instance.
Looks like [{attr1: [data], attr2: [data], label:'trick'}, {attr1: [data], label:'trick'}]
Each [data] is a list of 300 points of that attributes for each particular instance
"""
inst_list = []
attr_list = ['RTC time', 'AccX', 'AccY', 'AccZ', 'GyroX', 'GyroY', 'GyroZ',
             'MagX', 'MagY', 'MagZ']
attr_list2 = ['AccX', 'AccY', 'AccZ', 'MagX', 'MagY', 'MagZ', 'GyroX', 'GyroY', 'GyroZ']
label_index = 9
curr_attr_list = attr_list2
data_file = 'Collected_wrong.csv'#'O1.csv'#'tricks.csv'
feature_file = 'collected_wrong_feature.csv'#'collected_pop_feature.csv'#'tricks_without_corr.csv'#
with open(data_file, 'rb') as tricks_file:
    reader = csv.reader(tricks_file)
    curr_inst_dict = defaultdict(list)#instance_dictionary(attr_list)
    for row in reader:
        if len(row) == (label_index+1):
            prev_label = row[label_index]
            inst_list.append(curr_inst_dict)
            curr_inst_dict = defaultdict(list) #.clear()
            curr_inst_dict['label'] = prev_label
        for i, attr in enumerate(row):
            if i == label_index:
                continue
            if float(attr) != 0.0:
                curr_inst_dict[curr_attr_list[i]].append(float(attr))
        curr_row = row
    del inst_list[0]
    inst_list.append(curr_inst_dict)

"""
Write main script that will use feature_extractor to build features
and write to CSV
"""
extractor = fe.feature_extractor(inst_list, attr_list)
extractor.extractFeatures()
extractor.writeToCSV(feature_file)
