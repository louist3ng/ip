 Jason Chatbot – User Guide

Welcome to Jason, your personal task-tracking chatbot. This guide will help you get started quickly and make full use of Jason’s features.

## Getting Started

1\. Run Jason

Open a terminal in the project folder and type:

java -jar jason.jar

2\. Jason will greet you and wait for your commands.

## Features & Commands

### Add Tasks

• To-Do

todo &lt;description&gt;

Example: todo read book

• Deadline

deadline &lt;description&gt; /by &lt;dd/MM/yyyy HHmm&gt;

Example: deadline submit report /by 22/09/2025 1800

• Event

event &lt;description&gt; /from &lt;dd/MM/yyyy HHmm&gt; /to &lt;dd/MM/yyyy HHmm&gt;

Example: event project meeting /from 25/09/2025 1400 /to 25/09/2025 1600

### List Tasks

list

Shows all current tasks with their status.

### Mark / Unmark Tasks

• Mark task as done: mark &lt;task number&gt;

• Unmark task: unmark &lt;task number&gt;

### Delete Tasks

delete &lt;task number&gt;

### Find Tasks

find &lt;keyword&gt;

Example: find book

### Exit Jason

bye

Saves tasks and exits the program.

## Data Storage

• Jason automatically saves your tasks to a file (data/jason.txt).

• Your task list is restored the next time you run Jason.

## Tips

• Dates can usually be entered as dd/MM/yyyy HHmm (e.g., 22/09/2025 1800).

• Task numbers are shown when you use list.

• Use clear descriptions to make searching easier.

## Quick Reference

| Command Type | Format | Example |
| --- | --- | --- |
| To-Do | todo &lt;description&gt; | todo read book |
| Deadline | deadline &lt;description&gt; /by &lt;dd/MM/yyyy HHmm&gt; | deadline submit report /by 22/09/2025 1800 |
| Event | event &lt;description&gt; /from &lt;dd/MM/yyyy HHmm&gt; /to &lt;dd/MM/yyyy HHmm&gt; | event project meeting /from 25/09/2025 1400 /to 25/09/2025 1600 |
| List | list | list |
| Mark | mark &lt;task number&gt; | mark 2 |
| Unmark | unmark &lt;task number&gt; | unmark 2 |
| Delete | delete &lt;task number&gt; | delete 3 |
| Find | find &lt;keyword&gt; | find book |
| Exit | bye | bye |