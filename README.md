# jasperTest
## end-point: \
curl "http://localhost:9669/pdf/sfv/generate"

if jasper generates a big report, the memory usage will be high. the swapfile-virtualizer is used to reduce the memory usage, in that way, a swapfile is generated and saved under a directory, after report-generation. The cost of this is CPU-consumption and more generation-time. To reduce the generation-time, it is recommended to use a high-speed hard-disk. The random access is used in Swap-File reading and writing.
