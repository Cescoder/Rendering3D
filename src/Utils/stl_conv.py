import numpy as np
from stl import mesh

def read_stl_file(filename):
    """
    Reads an STL file and extracts the triangular facets.

    Args:
        filename (str): Path to the STL file.

    Returns:
        list: A list of triangles, where each triangle is represented by three vertices.
    """
    stl_mesh = mesh.Mesh.from_file(filename)
    triangles = []

    for i in range(len(stl_mesh.vectors)):
        triangle = stl_mesh.vectors[i]  # Extract the three vertices of the triangle
        triangles.append(triangle)

    return triangles

def scale_triangles(triangles, target_radius):
    """
    Scales the vertices of triangles to fit within a sphere of a given radius.

    Args:
        triangles (list): List of triangles.
        target_radius (float): The desired radius to scale the points to.

    Returns:
        list: List of scaled triangles.
    """
    # Flatten the list of triangles into a single array of points
    all_points = np.array([vertex for triangle in triangles for vertex in triangle])

    # Calculate the maximum distance from the origin
    max_distance = np.linalg.norm(all_points, axis=1).max()

    # Calculate the scaling factor
    scale_factor = target_radius / max_distance

    # Scale all points
    scaled_triangles = [triangle * scale_factor for triangle in triangles]

    return scaled_triangles

def write_triangles_to_file(triangles, filename):
    """
    Writes the triangles to a text file in the specified format.

    Args:
        triangles (list): List of triangles.
        filename (str): Path to the output text file.
    """
    with open(filename, 'w') as file:
        for triangle in triangles:
            # Format each triangle as x;y;z#x;y;z#x;y;z
            line = '#'.join([';'.join(map(str, vertex)) for vertex in triangle])
            file.write(line + '\n')

def main():
    # Input STL file path
    stl_file = 'input.stl'  # Replace with your STL file path

    # Output TXT file path
    txt_file = 'output.txt'  # Replace with your desired output file path

    # Target radius for scaling
    target_radius = 5.0  # Replace with your desired radius

    # Read STL file and extract triangles
    triangles = read_stl_file(stl_file)

    # Scale the triangles to fit within the desired radius
    scaled_triangles = scale_triangles(triangles, target_radius)

    # Write triangles to a text file
    write_triangles_to_file(scaled_triangles, txt_file)

    print(f"Triangles successfully written to {txt_file} with scaling applied to fit radius {target_radius}")

if __name__ == "__main__":
    main()