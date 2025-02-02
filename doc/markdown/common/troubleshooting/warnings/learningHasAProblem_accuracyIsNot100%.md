# CAUTION!  Learning has a problem. Accuracy is not 100%.

## Message

```
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
!!                                                                  !!
!! CAUTION!  Learning has a problem. Accuracy is not 100%.          !!
!!                                                                  !!
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
```

## Cause

Indicates that the accuracy did not reach 100% as a result of training with CreateML.

## Solution

### nan

Reboot you machine if you see **nan** in PRECISION RECALL section.

```
----------------------------------
Number of examples: 16
Number of classes: 2
Accuracy: 50.00%

******CONFUSION MATRIX******
----------------------------------
True\Pred [OFF] [ON]  
[OFF]     8     0     
[ON]      8     0     

******PRECISION RECALL******
----------------------------------
Class Precision(%) Recall(%)
[OFF] 50.00           100.00         
[ON]  nan             0.00            
```

### Insufficient training data

Try to provide sufficient amount of training data.

### Link

- [Error messages / Warning messages](../error_warning_messages.md)

