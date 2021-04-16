# CSC3002F Assignment 1: Chat App

## Members: Michael Scott, Michelle Lopes, Tuscany Botha

# Overview 

This application enables communication between two or more users in a multicast chat application. It uses the User Datagram Package at the transport layer.

This application is written entirely in Java.

# Getting Started

## makefile

A makefile has been included. Run the "make" command first. To run the server, run "make runServer". To start the client GUI, run "make runClient".

## Using the GUI

The GUI will open and request that the user enter their name and IP address. The name needs to be entered in the first box and the IP address of the machine hosting the server in the second. The chat will open and the user can begin communicating. 

## Notes

This program only works over a local network. Windows firewalls need to be disabled for the program to run properly.

# Capabilities - User

* **Group chat**: two or more users can use the chat app to communicate with each other.
* **Message receipt notification**: user is notified when their message is delivered to the recipient
* **List of users in chat**: a new user is informed of all the users in the chat when they join
* **GUI**: non-technical users can also use the chat app to communicate as no command line use is required

# Capabilities - Message Transport

* **Corruption check**: computed hash codes are compared at both client and server sides to confirm the message did not change during transport
* **Message sequence check**: every message's sequence number is compared to the user's message count
