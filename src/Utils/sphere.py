import math
import random 
# Funzione che genera i vertici della sfera
def generate_sphere(radius, subdivisions):
    vertices = []
    for i in range(subdivisions + 1):
        lat = math.pi * i / subdivisions  # latitudine
        for j in range(subdivisions * 2):
            lon = 2 * math.pi * j / (subdivisions * 2)  # longitudine
            x = radius * math.sin(lat) * math.cos(lon)
            y = radius * math.sin(lat) * math.sin(lon)
            z = radius * math.cos(lat)
            vertices.append((x, y, z))
    return vertices

# Funzione che genera i triangoli della sfera
def generate_triangles(vertices, subdivisions):
    triangles = []
    for i in range(subdivisions):
        for j in range(subdivisions * 2):
            # Indici dei vertici
            current = i * (subdivisions * 2) + j
            next_lat = (i + 1) * (subdivisions * 2) + j
            next_lon = (j + 1) % (subdivisions * 2)
            next_current = i * (subdivisions * 2) + next_lon
            next_next_lat = (i + 1) * (subdivisions * 2) + next_lon
            
            # Triangoli: (current, next_lat, next_current) e (next_lat, next_next_lat, next_current)
            triangles.append((vertices[current], vertices[next_lat], vertices[next_current]))
            triangles.append((vertices[next_lat], vertices[next_next_lat], vertices[next_current]))

    return triangles

# Funzione che scrive i triangoli su un file txt nel formato richiesto
def write_triangles_to_file(triangles, filename):
    with open(filename, 'w') as file:
        for triangle in triangles:
            # Scrive ogni triangolo come x;y;z#x;y;z#x;y;z
            line = '#'.join([';'.join(map(str, vertex)) for vertex in triangle])
            file.write(line + '\n')

# Parametri
radius = 4  # raggio della sfera
subdivisions = 10  # numero di suddivisioni

# Genera i vertici e i triangoli
vertices = generate_sphere(radius, subdivisions)
triangles = generate_triangles(vertices, subdivisions)

# Scrivi i triangoli su un file di testo
write_triangles_to_file(triangles, f'sphere_data_{radius}_{subdivisions}.txt')

print("Triangoli scritti su ",  f'sphere_data_{radius}_{subdivisions}.txt')
