import subprocess
import logging
import os

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
        else :
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
    if (not os.path.isfile('compile')):
        print("No compiler executable exists.")
        exit(-1)
    if (not os.path.isdir('wacc_examples')):
        p = subprocess.Popen('git clone https://gitlab.doc.ic.ac.uk/lab1920_spring/wacc_examples.git', shell=True, stdout=subprocess.PIPE)
        stdout, stderr = p.communicate()
    test_valid()
    test_semantic_err()
    test_syntax_error()
    print("\nPassed {}/{} cases".format(n_passed, n_cases))
    print("Failed on:")
    for file_name in failed_cases:
        print(file_name)


