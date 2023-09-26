# Import required packages
import csv
from datetime import datetime
import json
import subprocess

# Import configuration
import benchmarks
import datasets

# Some local configuration definitions
aethra_jar_path = '/home/olivier/Repositories/AethraDB/target/AethraDB.jar'
java_executable_path = '/usr/lib/jvm/openjdk-20.0.1/bin/java'

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
csv_header = ['Benchmark', 'Dataset', 'Scale-Factor', 'Paradigm', 'Repetitions', 'ResultSummarised', 'Planning-Time', 'Codegen-Time', 'Compilation-Time', 'Execution-Time']

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

                total_planning_time = 0.0
                total_codegen_time = 0.0
                total_compilation_time = 0.0
                total_execution_time = 0.0
                
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
                            total_planning_time += timing_information["planning"]
                            total_codegen_time += timing_information["codegen"]
                            total_compilation_time += timing_information["compilation"]
                            total_execution_time += timing_information["execution"]
                            break

                average_planning_time = round(total_planning_time / number_repetitions, 1)
                average_codegen_time = round(total_codegen_time / number_repetitions, 1)
                average_compilation_time = round(total_compilation_time / number_repetitions, 1)
                average_execution_time = round(total_execution_time / number_repetitions, 1)
                print('Finished executing current benchmark configuration!')
                print('Average planning time was ' + str(average_planning_time) + 'ms.')
                print('Average codgen time was ' + str(average_codegen_time) + 'ms.')
                print('Average compilation time was ' + str(average_compilation_time) + 'ms.')
                print('Average execution time was ' + str(average_execution_time) + 'ms.\n\n')

                # Write a line to the benchmark output file
                csv_writer.writerow([
                    benchmark_name,
                    benchmark_dataset,
                    benchmark_scale_factor,
                    benchmark_paradigm,
                    number_repetitions,
                    summarise_result,
                    average_planning_time,
                    average_codegen_time,
                    average_compilation_time,
                    average_execution_time
                ])

# Clean up
csv_file.close()