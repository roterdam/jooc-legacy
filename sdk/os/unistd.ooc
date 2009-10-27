//import structs/ArrayList

include unistd | (__USE_GNU)
//include linux/limits

PIPE_BUF: extern Int
STDOUT_FILENO: extern Int
STDERR_FILENO: extern Int

/* Functions */
dup2: extern func(Int, Int) -> Int
execv: extern func(String, String*) -> Int
execvp: extern func(String, String*) -> Int
execve: extern func(String, String*, String*) -> Int
fileno: extern func(FILE*) -> Int
fork: extern func -> Int
pipe: extern func(arg: Int*) -> Int
  
