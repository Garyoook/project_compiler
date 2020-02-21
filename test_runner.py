import subprocess
import logging
import os
import sys

failed_cases = []
n_passed = 0
n_cases = 0


semantic_error_code = 200
syntax_error_code = 100
test_dir = os.path.abspath(os.path.join(os.path.dirname(__file__), 'wacc_examples'))
valid_file_dir = os.path.join(test_dir, 'valid')
invalid_file_dir = os.path.join(test_dir, 'invalid/syntaxErr')
semantic_error_dir = os.path.join(test_dir, 'invalid/semanticErr')


def test_valid():
    files = traverse(valid_file_dir)
    for f in files:
        compile(f, code=0)


def test_syntax_error():
    files = traverse(invalid_file_dir)
    for f in files:
        compile(f, code=syntax_error_code)


def test_semantic_err():
    files = traverse(semantic_error_dir)
    for f in files:
        compile(f, code=semantic_error_code)


def test_backend():
    files = traverse(valid_file_dir)
    for f in files:
        compile_backend(f)


def compile_backend(filename):
    global n_passed, n_cases
    print("Running {}".format(filename))
    n_cases += 1
    os.system("./compile " + filename)
    filename = filename.split(".wacc")[0]
    temp = filename.split("/")
    filename = temp[len(temp)-1]
    os.system("arm-linux-gnueabi-gcc -o " + filename + " -mcpu=arm1176jzf-s -mtune=arm1176jzf-s " + os.path.dirname(__file__) + filename + ".s")
    try:
        if (filename == "IOSequence"):
            subprocess.PIPE = "1"
            p = subprocess.Popen("qemu-arm -L /usr/arm-linux-gnueabi/ " + filename, stdin="1", shell=True)
            stdout, stderr = p.communicate()
            print(stdout)
            print(stderr)
        else:
            output = subprocess.check_output("qemu-arm -L /usr/arm-linux-gnueabi/ " + filename,
                                              stderr=subprocess.STDOUT, shell=True)
            print(output)
            sample_output = subprocess.check_output("qemu-arm -L /usr/arm-linux-gnueabi/ " + filename,
                                             stderr=subprocess.STDOUT, shell=True)

        n_passed += 1
        print("Passed {}".format(filename))
        # print(stdout)
        # subprocess.check_output("./compile " + filename,
        #                         stderr=subprocess.STDOUT, shell=True)
    #     if code != 0:

    #     else:

    except subprocess.CalledProcessError as e:
        print("Failed on {}".format(filename))
        failed_cases.append(filename)

    #     if e.returncode != code:
    #         print("Failed on {}".format(filename))
    #         failed_cases.append(filename)
    #     else:
    #         n_passed += 1
    #         print("Passed {}".format(filename))


def compile(filename, code=0):
    global n_passed, n_cases
    print("Running {}".format(filename))
    n_cases += 1
    try:
        subprocess.check_output("./compile " + filename,
                                stderr=subprocess.STDOUT, shell=True)
        if code != 0:
            print("Failed on {}".format(filename))
            failed_cases.append(filename)
        else:
            n_passed += 1
            print("Passed {}".format(filename))
    except subprocess.CalledProcessError as e:
        if e.returncode != code:
            print("Failed on {}".format(filename))
            failed_cases.append(filename)
        else:
            n_passed += 1
            print("Passed {}".format(filename))
        

def traverse(file_dir):
    files = []

    def flatten(directory):
        for dirpath, _, filenames in os.walk(directory):
            for f in filenames:
                if f.endswith('.wacc'):
                    files.append(os.path.join(dirpath, f))
    if isinstance(file_dir, str):
        file_dir = [file_dir]
    for item in file_dir:
        if os.path.isdir(item):
            flatten(item)
        else:
            files.append(item)
    return files


if __name__ == '__main__':
    if not os.path.isfile('compile'):
        print("No compiler executable exists.")
        exit(-1)
    if not os.path.isdir('wacc_examples'):
        p = subprocess.Popen('git clone https://gitlab.doc.ic.ac.uk/lab1920_spring/wacc_examples.git', shell=True, stdout=subprocess.PIPE)
        stdout, stderr = p.communicate()
    os.system("make")
    if sys.argv[1] == "valid":
        test_valid()
    if sys.argv[1] == "semantic":
        test_semantic_err()
    if sys.argv[1] == "syntax":
        test_syntax_error()
    if sys.argv[1] == "all":
        test_valid()
        test_syntax_error()
        test_semantic_err()
    if sys.argv[1] == "backend":
        test_backend()
    print("\nPassed {}/{} cases".format(n_passed, n_cases))
    if len(failed_cases) == 0:
        print("Failed on: 0 cases")
    else:
        print("Failed on:")
    for file_name in failed_cases:
        print(file_name)


