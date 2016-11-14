import tensorflow as tf

#For now only 1 file, but leaving it in queue format in case we add more later
filename_queue = tf.train.string_input_producer(
    [("tricks%i.csv") % i for i in range(1)])

reader = tf.TextLineReader()
key, value = reader.read(filename_queue)

record_defaults = [[0],[0],[0],[0],[0],[0],[0],[0],[0],[0]]
