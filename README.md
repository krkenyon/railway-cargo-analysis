# railway-cargo-analysis


A railway system consists of several stations connected by one-way tracks.

Each station is associated with two cargo types:

One type that is unloaded when a train arrives.

One type that is loaded before a train departs.

All trains start from the same initial station, carrying no cargo. Trains may follow any valid route along the tracks.

Determine, for each station, which cargo types might be on a train when it arrives. A cargo type is considered possible if there is at least one route from the initial station that brings it to the station.

Rules

When a train arrives at a station, first it unloads the cargo type this station consumes, then it loads the cargo type this station 
provides.

Trains can carry multiple cargo types at the same time.

Cargo types are abstract labels; the amount does not matter.

Input Format

The first line contains two integers: S, T — the number of stations and tracks.

The next S lines each contain three integers: s, c_unload, c_load, where

s — the id of a station

c_unload — the kind of goods unloaded at station s

c_load — the kind of goods loaded at the station s

The next T lines each contain two integers: s_from, s_to indicating a directed track from station s_from to station s_to.

The last line contains an integer s_0, the starting station.
