# Rendering3D

A simple 3D rendering engine in Java using matrix transformations and projections.

## Features
- 3D object representation using polygons
- Matrix-based transformations (scaling, rotation, translation)
- Hierarchical object structure (parent-child relationships)
- Projection onto a 2D plane for visualization
- Basic rendering using Java Swing

## Project Structure
```
Rendering3D/
│── src/
│   ├── Matrix.java       # Matrix operations and transformations
│   ├── Object3D.java     # 3D object representation
│   ├── Polygon.java      # Polygon structure with vertices
│   ├── Scene.java        # Scene management (inherits from Object3D)
│   ├── Panel.java        # Rendering logic using Java Swing
│── README.md             # Project documentation
```

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher

## Usage
- Modify `App.java` to add objects to the scene.
- Use matrix transformations in `Matrix.java` to manipulate objects.
- Update `Panel.java` to customize rendering settings.

## Contributing
Contributions are welcome! Feel free to fork the repository and submit pull requests.

## License
This project is licensed under the MIT License.

## Author
[gass-ita](https://github.com/gass-ita)
[Cescoder](https://github.com/cescoder)

