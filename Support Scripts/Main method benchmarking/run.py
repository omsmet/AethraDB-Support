# Import required packages
import csv
from datetime import datetime
import subprocess
import time

# Import configuration
import benchmarks
import datasets

# Some local configuration definitions
aethra_jar_path = '/home/olivier/Repositories/AethraDB/target/AethraDB.jar'
java_executable_path = '/usr/lib/jvm/java-20-openjdk/bin/java'

# aethra_jar_path = '/home/olivier/AethraDB/AethraDB.jar'
# java_executable_path = '/home/olivier/external_software/openjdk-20.0.1/bin/java'

java_options = [
    java_executable_path,
    "--add-opens=java.base/java.nio=ALL-UNNAMED",
    "--add-modules=jdk.incubator.vector",
    "--enable-preview",
    "-Darrow.enable_unsafe_memory_access=true",
    "-Darrow.enable_null_check_for_get=false",
    "-Xmx32g",
    "-Xms16g",
]

number_repetitions = 10

# Constant definitions
csv_header = ['Benchmark', 'Dataset', 'Scale-Factor', 'Paradigm', 'Running-Time', 'Repetitions', 'ResultSummarised']

# Initialise CSV writer
file_name = 'main_method_benchmark_result_{date:%Y-%m-%d_%H:%M:%S}.csv'.format( date = datetime.now() )
csv_file = open(file_name, 'w')
csv_writer = csv.writer(csv_file)
csv_writer.writerow(csv_header)

# Execute all of the benchmarks
for benchmark_name, benchmark_definition in benchmarks.benchmarks.items():

    # Obtain query file path
    query_path = benchmark_definition['query_file']

    # Execute the benchmark for each dataset
    for benchmark_dataset in benchmark_definition['datasets']:

        # Execute for each scale factor of the dataset
        for benchmark_scale_factor in benchmark_definition['scale_factors']:

            # Obtain the dataset path
            dataset_path = datasets.datasets[benchmark_dataset][benchmark_scale_factor]

            # Execute for each paradigm
            for benchmark_paradigm in benchmark_definition['paradigms']:

                print(
                    'Executing benchmark \''
                    + benchmark_name
                    + '\' on dataset \''
                    + benchmark_dataset
                    + '\' at scale-factor '
                    + str(benchmark_scale_factor)
                    + ' using ' + benchmark_paradigm + ' paradigm ...')
                
                benchmark_arguments = java_options + [
                    "-jar", aethra_jar_path,
                    "-d", dataset_path,
                    "-q", 'queries/' + query_path,
                    "-p", benchmark_paradigm
                ]

                summarise_result = benchmark_definition['summarise']
                if summarise_result:
                    benchmark_arguments = benchmark_arguments + ['-s']

                total_benchmark_time = 0.0
                
                # Repeat the experiment number_repetitions times
                for i in range(number_repetitions):
                    print('Benchmarking iteration ' + str(i) + ' ...')

                    start_time = time.time_ns()

                    benchmark_process = subprocess.call(
                        benchmark_arguments,
                        stdout = subprocess.DEVNULL,
                        stderr = subprocess.DEVNULL)
                    #benchmark_process.wait()

                    end_time = time.time_ns()
                    elapsed_time = (end_time - start_time) / 1_000_000
                    print('Iteration ' + str(i) + ' took ' + str(round(elapsed_time, 1)) + 'ms.')
                    total_benchmark_time += elapsed_time
                
                average_benchmark_time = round(total_benchmark_time / number_repetitions, 1)
                print('Finished executing current benchmark configuration!')
                print('Average execution time was ' + str(average_benchmark_time) + 'ms.\n\n')

                # Write a line to the benchmark output file
                csv_writer.writerow([
                    benchmark_name,
                    benchmark_dataset,
                    benchmark_scale_factor,
                    benchmark_paradigm,
                    average_benchmark_time,
                    number_repetitions,
                    summarise_result
                ])

# Clean up
csv_file.close()