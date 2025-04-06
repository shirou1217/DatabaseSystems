# Database Systems Course Projects

This repository contains six projects completed during my Database Systems course. Each project focuses on different aspects of database system implementation, optimization, and benchmarking using the VanillaDB system.

## Table of Contents

- [Final Project: Vector Similarity Search Implementation](#final-project-vector-similarity-search-implementation)
- [Assignment 5: Conservative Locking Implementation](#assignment-5-conservative-locking-implementation)
- [Assignment 4: Buffer and File Manager Optimization](#assignment-4-buffer-and-file-manager-optimization)
- [Assignment 3: Query Explain Plan Implementation](#assignment-3-query-explain-plan-implementation)
- [Assignment 2: Transaction Implementation](#assignment-2-transaction-implementation)
- [Assignment 1: Database Schema Design](#assignment-1-database-schema-design)

## Final Project: Vector Similarity Search Implementation

**Technologies & Concepts:**
- IVF_FLAT vector search algorithm implementation
- SIMD (Single Instruction, Multiple Data) optimization
- K-means clustering algorithm
- High-dimensional vector operations (Euclidean distance)
- Performance profiling & benchmarking
- Data preprocessing techniques
- Batch processing optimizations

**What I Learned:**
- Implemented an IVF_FLAT indexing algorithm for high-dimensional vector search
- Created a complete pipeline: data preprocessing, training (using k-means), and evaluation
- Applied SIMD optimization to improve vector distance calculation performance
- Analyzed and improved system performance through profiling
- Experimented with different parameter configurations to achieve optimal recall vs speed
- Optimized data insertion operations by using random cluster assignment during initialization
- Reduced vector insertion time from 20 minutes to 10 minutes (2x improvement)

## Assignment 5: Conservative Locking Implementation

**Technologies & Concepts:**
- Conservative locking concurrency control
- Transaction management
- Deadlock prevention
- Lock request queuing
- Primary key-based locking
- Buffer pool optimization

**What I Learned:**
- Implemented conservative locking for DBMS concurrency control
- Created a lock table with request queues for efficient lock management
- Developed X-lock and S-lock mechanisms for read/write operations
- Built a system for pre-defining read/write sets before transaction execution
- Implemented atomic operations for concurrent access
- Analyzed the impact of buffer pool size on transaction throughput
- Eliminated transaction aborts by preventing deadlocks through conservative locking
- Learned the trade-offs between parallelism and transaction safety

## Assignment 4: Buffer and File Manager Optimization

**Technologies & Concepts:**
- Concurrent buffer management
- Atomic operations
- Lock granularity optimization
- File I/O optimization
- Java concurrency utilities 
- Performance benchmarking

**What I Learned:**
- Optimized buffer management by replacing coarse-grained synchronization with fine-grained locks
- Implemented atomic operations using Java's `AtomicInteger` for pin counting
- Utilized `ReentrantLock` for more precise locking control
- Optimized file operations by removing unnecessary synchronization from read operations
- Improved block ID calculation efficiency through caching computed values
- Achieved approximately 2500 more committed transactions and reduced latency by 1ms
- Conducted performance analysis under TPC-C benchmark
- Gained practical experience with concurrency optimization in database systems

## Assignment 3: Query Explain Plan Implementation

**Technologies & Concepts:**
- SQL query execution plan visualization
- Query parsing and lexing
- SQL execution plans
- Plan tree representation

**What I Learned:**
- Extended a SQL parser to recognize the EXPLAIN keyword
- Implemented query plan visualization for different types of SQL operations
- Created a plan tree representation with proper indentation levels
- Built specialized plan handlers for different query operations:
  - Single table access with WHERE clauses
  - Multi-table joins with WHERE clauses
  - Queries with ORDER BY clauses
  - Queries with GROUP BY and aggregate functions
- Gained deeper understanding of SQL query execution processes

## Assignment 2: Transaction Implementation

**Technologies & Concepts:**
- Database transaction processing
- JDBC vs stored procedures
- Performance benchmarking
- Read/write transaction mix optimization

**What I Learned:**
- Implemented update price transactions in a benchmark system
- Created both JDBC and stored procedure implementations
- Developed parameter generators and transaction executors
- Analyzed performance differences between JDBC and stored procedures
- Experimented with different read/write transaction ratios (90/10, 50/50, 0/100)
- Observed that stored procedures consistently outperform JDBC implementations
- Understood transaction latency and throughput trade-offs
- Learned about the overhead of JDBC connections vs direct database operations

## Assignment 1: Database Schema Design

**Technologies & Concepts:**
- Entity-Relationship (ER) modeling
- Relational schema design
- SQL DDL (Data Definition Language)
- Primary and foreign key constraints

**What I Learned:**
- Created ER models for academic project management and forum systems
- Translated ER models to relational schemas
- Developed SQL DDL statements for schema creation
- Applied proper primary and foreign key constraints
- Designed many-to-many relationships using junction tables
- Practiced normalization techniques to ensure data integrity

---

These projects have provided me with comprehensive hands-on experience in database system implementation, optimization, and design. From low-level buffer and file management to high-level query planning and specialized vector search algorithms, I've gained practical knowledge of database internals and performance optimization techniques.
