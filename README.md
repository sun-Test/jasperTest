# jasperTest
## end-point: \
curl "http://localhost:9669/pdf/sfv/generate"
## description:
if jasper generates a big report, the memory usage will be high. the swapfile-virtualizer is used to reduce the memory usage, in that way, a swapfile is generated and saved under a directory, after report-generation. The cost of this is CPU-consumption and more generation-time. To reduce the generation-time, it is recommended to use a high-speed hard-disk. The random access is used in Swap-File reading and writing.\
## parameter in .properties:
- jasper.swapfile.directory: the out-put directory of the swap-file
- jasper.swapfile.maxsize: the max-size of pages, that is allowed to store in the primary memory.
- jasper.swapfile.blocksize: the block size of the swap-file
- jasper.swapfile.mingrow: the min-grow-count of blocks, if a swap-file is full. (the grow-size will be block * min-grow-count)
