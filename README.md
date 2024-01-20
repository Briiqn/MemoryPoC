# MemoryPoC

MemoryPoC is a Proof of Concept (PoC) ransomware concept that takes a unique approach by encrypting data directly in memory rather than traditional file-based encryption. This concept is designed to leverage the ample memory available in modern systems for storing encrypted data.

## Running in a Virtual Machine

Please note that MemoryPoC is intended to be executed in a virtual machine environment. Running it on a physical machine may lead to unintended consequences. Make sure to set up a suitable virtual environment before testing the concept.This also requires a Java Runtime >=21

## Origins and Inspiration

The idea for MemoryPoC originated from casual musings, often referred to as "toilet thoughts." The concept was born out of contemplating alternative approaches to ransomware and exploring the feasibility of encrypting data directly in memory.

## How MemoryPoC Differs from Traditional Ransomware

Traditional ransomware typically encrypts files on the disk, rendering them inaccessible until a ransom is paid. MemoryPoC, on the other hand, encrypts data directly in the computer's memory. This unique approach offers several distinctions:

### 1. In-Memory Encryption

MemoryPoC does not rely on encrypting files stored on the disk. Instead, it encrypts data directly in the computer's volatile memory. This can make detection and analysis more challenging as the encrypted data is not stored persistently on the disk.

### 2. Rapid Data Loss Threat

One significant distinction is the immediate threat of data loss. Since MemoryPoC encrypts data in memory, victims could potentially lose all their data in an instant if they don't comply with the ransom demand. This pressure tactic aims to expedite the payment process.

### 3. Counteracting Traditional Defense Mechanisms

MemoryPoC goes against traditional advice to turn off the computer when faced with a potential ransomware attack. Turning off the computer won't prevent data loss since the encrypted data resides in memory, not on the disk. This adds a layer of complexity for users trying to defend against the attack.

### 4. Reduced Footprint

By operating solely in memory, MemoryPoC reduces its footprint on the disk. Traditional ransomware may leave traces in the form of encrypted files, making it easier to identify and mitigate. MemoryPoC aims to operate more discreetly.

## Disclaimer

The developer of MemoryPoC is not responsible for any damage caused by the execution of this concept. It is essential to use caution and run MemoryPoC only in a controlled and isolated environment, such as a virtual machine dedicated for testing purposes.

## Encryption Algorithm

MemoryPoC utilizes a robust 2048-bit RSA encryption algorithm to secure the data stored in memory. This encryption algorithm is known for its strength and is commonly used in cryptographic applications.

Please use MemoryPoC responsibly and ethically for research and educational purposes only.
