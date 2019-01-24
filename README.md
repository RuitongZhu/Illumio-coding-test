# Illumio-coding-test

## Configuration

The original code for `Firewall` class is under the `src` repository, and is composed of the class itself and an unit test.

## Explanation

#### test
Due to time limit, there is only one unit test implemented, which is exactly the example given in the assignment.

#### thoughts
Personally I believe the key point here is how to deal with the range and the possible overlapping of the third parameter `port`. At first I was thinking about using a data structure like a trie for indexing the port number, but when I was half way on coding, I realized that it is too complicated to finish it on time. I start over to figure out a simplier way. Since the port number is limited (65535), I just create an array of that size for faster indexing.

#### structure
There are two inner class: `PortNode` and `IPNode`. 
`PortNode` keeps track of four lists of `IPNode`s corresponding to "inbound-tcp", "inbound-udp", "outbound-tcp" and "outbound-udp" respectively. I seperate these `IPNode`s to decrease the checking process.
`IPNode` is referenced from certain `PortNode`. Each instance of `IPNode` represents a rule from the csv file and provide the functionality to check whether a given IP address match this rule.

#### complexity
The initialization of the `Firewall` class may run in `O(mn)`, where `m` indicates the number of rules in the csv file, and `n` indicates the number of incoming inputs. The runtime grows when the range for each rule is wide and there are overlappings in between. This is for the sake of faster checking of streaming input and the check of `accept_packet` runs in `O(m)`. The worst case happens when all the rules overlap with each other on that certain port, but if there is no overlapping, the function runs in `O(1)`. 

The space is most occupied by the array of `PortNode` : `ports`. The size of the array is 65535. It is pretty large but firely acceptable.

#### pros & cons
The implementation provides first checking given input arguments, but the initilization might be slow since it iterates all the port numbers given a range and initialize an `IPNode` for it. The space complexity, as discussed above, is not ideal as well.

#### possible improvements
To improve the performance, the first idea came to me is not to initialize an `IPNode` for each `PortNode` but for each rule, and I just need to reference it in `PortNode` to save space. The second idea is to implement it as a trie. It will be much space efficient but I have got no time for it in this test.

## Team preference
I am interested in joining Platform Team or Data Team.
