The following table summarises the UserLinux run levels:
* 0            System Halt
* 1            Single user
* 2            Full multi-user mode (Default)
* 3-5          Same as 2
* 6            System Reboot


Run Level 1 is known as 'single user' mode. A more apt description would be 'rescue', or 'trouble-shooting' mode. In run level 1, no daemons (services) are started. Hopefully single user mode will allow you to fix whatever made the transition to rescue mode necessary.

(You can boot into single user mode typically by using your boot loader, lilo or grub, to add the word 'single' to the end of the kernel command line).

Run levels 2 through 5 are full multi-user mode and are the same in a default UserLinux (Debian) system. It is a common practise in other Linux distributions to use run level 3 for a text console login and run level 5 for a graphical login.

Run level 6 is used to signal system reboot. This is just like run level 0 except a reboot is issued at the end of the sequence instead of a power off.


See chkconfig, sysv-rc-conf, update-rc.d

https://debian-administration.org/article/212/An_introduction_to_run-levels