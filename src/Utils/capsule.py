import math

def generate_capsule(radius, height, subdivisions):
    vertices = []
    
    # Genera la semisfera superiore
    for i in range(subdivisions // 2 + 1):
        lat = (math.pi / 2) * i / (subdivisions // 2)  # Da 0 a pi/2 per la cupola
        for j in range(subdivisions * 2):
            lon = 2 * math.pi * j / (subdivisions * 2)
            x = radius * math.sin(lat) * math.cos(lon)
            y = radius * math.sin(lat) * math.sin(lon)
            z = radius * math.cos(lat) + height / 2  # Spostata in alto
            vertices.append((x, y, z))
    
    # Genera il cilindro centrale
    for i in range(subdivisions + 1):
        z = height / 2 - (height * i / subdivisions)
        for j in range(subdivisions * 2):
            lon = 2 * math.pi * j / (subdivisions * 2)
            x = radius * math.cos(lon)
            y = radius * math.sin(lon)
            vertices.append((x, y, z))
    
    # Genera la semisfera inferiore
    for i in range(1, subdivisions // 2 + 1):
        lat = (math.pi / 2) * i / (subdivisions // 2)
        for j in range(subdivisions * 2):
            lon = 2 * math.pi * j / (subdivisions * 2)
            x = radius * math.sin(lat) * math.cos(lon)
            y = radius * math.sin(lat) * math.sin(lon)
            z = -radius * math.cos(lat) - height / 2  # Spostata in basso
            vertices.append((x, y, z))
    
    return vertices

def generate_triangles(vertices, subdivisions):
    triangles = []
    rows = subdivisions // 2 + 1 + subdivisions + subdivisions // 2 + 1
    cols = subdivisions * 2
    
    for i in range(rows - 1):
        for j in range(cols):
            current = i * cols + j
            next_row = (i + 1) * cols + j
            next_col = (j + 1) % cols
            next_current = i * cols + next_col
            next_next_row = (i + 1) * cols + next_col
            
            # Primo triangolo
            triangles.append((vertices[current], vertices[next_row], vertices[next_current]))
            # Secondo triangolo
            triangles.append((vertices[next_row], vertices[next_next_row], vertices[next_current]))
    
    return triangles

def write_triangles_to_file(triangles, filename):
    with open(filename, 'w') as file:
        for triangle in triangles:
            line = '#'.join([';'.join(map(str, vertex)) for vertex in triangle])
            file.write(line + '\n')

radius = 1  # Raggio della capsula
height = 2  # Altezza del cilindro centrale
subdivisions = 10  # Livello di suddivisione

vertices = generate_capsule(radius, height, subdivisions)
triangles = generate_triangles(vertices, subdivisions)
write_triangles_to_file(triangles, f'capsule_data_{radius}_{height}_{subdivisions}.txt')

print("Triangoli scritti su", f'capsule_data_{radius}_{height}_{subdivisions}.txt')
