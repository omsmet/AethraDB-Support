# Import required packages
import csv
from datetime import datetime
import json
import subprocess
import time

# Import configuration
import benchmarks
import datasets

# Some local configuration definitions
aethra_jar_path = '/home/olivier/Repositories/AethraDB/target/AethraDB.jar'
# aethra_jar_path = '/nvtmp/AethraDB.jar'
java_executable_path = '/usr/lib/jvm/java-21-openjdk/bin/java'
# java_executable_path = '/usr/lib/jvm/graalvm-jdk-21+35.1/bin/java'
# java_executable_path = '/usr/lib/jvm/graalvm-community-openjdk-21+35.1/bin/java'

# aethra_jar_path = '/home/olivier/AethraDB/AethraDB.jar'
# java_executable_path = '/home/olivier/external_software/graalvm-jdk-21+35.1/bin/java'

java_options = [
    java_executable_path,
    "--add-opens=java.base/java.nio=ALL-UNNAMED",
    "--enable-preview",
    "-Darrow.enable_unsafe_memory_access=true",
    "-Darrow.enable_null_check_for_get=false",
    "-Xmx32g",
    "-Xms16g",
]

number_repetitions = 10

# Constant definitions
csv_header = ['Benchmark', 'Dataset', 'Scale-Factor', 'Paradigm', 'Iteration', 'ResultSummarised', 'Planning-Time', 'Codegen-Time', 'Compilation-Time', 'Execution-Time', 'Total-Time']

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
                    "-p", benchmark_paradigm,
                    "-t"
                ]

                summarise_result = benchmark_definition['summarise']
                if summarise_result:
                    benchmark_arguments = benchmark_arguments + ['-s']
                
                # Repeat the experiment number_repetitions times
                for i in range(number_repetitions):
                    print('Benchmarking iteration ' + str(i) + ' ...')

                    benchmark_process = subprocess.Popen(
                        benchmark_arguments,
                        stdout = subprocess.DEVNULL,
                        stderr = subprocess.PIPE)
                    benchmark_process.wait()

                    # Extract the timing information from std.err
                    for line in iter(benchmark_process.stderr.readline, b''):
                        decoded_line = line.decode().rstrip()

                        # Detect the line that has the actual timing information
                        if decoded_line[0] == '{' and decoded_line[-1] == '}':
                            timing_information = json.loads(decoded_line)
                            planning_time = round(timing_information["planning"], 1)
                            codegen_time = round(timing_information["codegen"], 1)
                            compilation_time = round(timing_information["compilation"], 1)
                            execution_time = round(timing_information["execution"], 1)
                            total_time = round(timing_information["total"], 1)

                            # Write a line to the benchmark output file
                            csv_writer.writerow([
                                benchmark_name,
                                benchmark_dataset,
                                benchmark_scale_factor,
                                benchmark_paradigm,
                                i,
                                summarise_result,
                                planning_time,
                                codegen_time,
                                compilation_time,
                                execution_time,
                                total_time
                            ])

                            break

                print('Finished executing current benchmark configuration!\n\n')

# Clean up
csv_file.close()
