## Overview

This project analyses cargo propagation in a railway network.

Each station loads a cargo type and may unload another cargo type.
Cargo propagates through the railway network along tracks.

The goal is to compute, for every station, the set of cargo types that
can arrive there starting from a given initial station.

The analysis correctly handles:
- branching railway networks
- cycles in the graph
- stations that unload specific cargo types

## Input Format

The input describes a railway system.

```
N M

station_id unload_type load_type

...

from_station to_station

...

start_station
```

Where:

- `N` is the number of stations
- `M` is the number of tracks

Each station line specifies:
station_id unload_cargo load_cargo

Each track line specifies a directed edge:
from_station to_station

The final line specifies the start station.

## Algorithm

The railway network is treated as a directed graph.

Cargo propagation is computed using a worklist-based dataflow algorithm.

For each station we maintain:

- `inCargo`: cargo that can arrive at the station
- `outCargo`: cargo that leaves the station after unloading and loading

Propagation proceeds by repeatedly updating neighbouring stations until
no new cargo can be added (a fixed point is reached).

This approach guarantees correctness even when the graph contains cycles.

## Running

Build the project:

./gradlew build

Run the program:

./gradlew run < input.txt

## Tests

Unit tests are provided for:

- Cargo propagation analysis
- Input parsing
- End-to-end integration

Run tests with:

./gradlew test

## Design

The implementation is structured into three main components:

- `RailwaySystem` – data model of stations and tracks
- `parseSystem` – parser for the input format with validation
- `CargoAnalysis` – worklist-based algorithm computing cargo propagation
