import math

# Funzione che genera i vertici del toro
def generate_torus(major_radius, minor_radius, subdivisions_major, subdivisions_minor):
    vertices = []
    for i in range(subdivisions_major):
        theta = 2 * math.pi * i / subdivisions_major  # angolo principale
        for j in range(subdivisions_minor):
            phi = 2 * math.pi * j / subdivisions_minor  # angolo secondario

            # Coordinate del toro
            x = (major_radius + minor_radius * math.cos(phi)) * math.cos(theta)
            y = (major_radius + minor_radius * math.cos(phi)) * math.sin(theta)
            z = minor_radius * math.sin(phi)

            vertices.append((x, y, z))
    return vertices

# Funzione che genera i triangoli del toro
def generate_torus_triangles(vertices, subdivisions_major, subdivisions_minor):
    triangles = []
    for i in range(subdivisions_major):
        for j in range(subdivisions_minor):
            # Indici dei vertici
            current = i * subdivisions_minor + j
            next_major = ((i + 1) % subdivisions_major) * subdivisions_minor + j
            next_minor = i * subdivisions_minor + (j + 1) % subdivisions_minor
            next_major_minor = ((i + 1) % subdivisions_major) * subdivisions_minor + (j + 1) % subdivisions_minor

            # Triangoli: (current, next_major, next_minor) e (next_major, next_major_minor, next_minor)
            triangles.append((vertices[current], vertices[next_major], vertices[next_minor]))
            triangles.append((vertices[next_major], vertices[next_major_minor], vertices[next_minor]))

    return triangles

# Funzione che scrive i triangoli su un file txt nel formato richiesto
def write_triangles_to_file(triangles, filename):
    with open(filename, 'w') as file:
        for triangle in triangles:
            # Scrive ogni triangolo come x;y;z#x;y;z#x;y;z
            line = '#'.join([';'.join(map(str, vertex)) for vertex in triangle])
            file.write(line + '\n')

# Parametri
major_radius = 2.0  # Raggio principale del toro
minor_radius = 1.0  # Raggio secondario del toro
subdivisions_major = 30  # Suddivisioni lungo il raggio principale
subdivisions_minor = 30  # Suddivisioni lungo il raggio secondario

# Genera i vertici e i triangoli del toro
vertices = generate_torus(major_radius, minor_radius, subdivisions_major, subdivisions_minor)
triangles = generate_torus_triangles(vertices, subdivisions_major, subdivisions_minor)

# Scrivi i triangoli su un file di testo
write_triangles_to_file(triangles, f'torus_data_{major_radius}_{minor_radius}_{subdivisions_major}_{subdivisions_minor}.txt')

print("Triangoli del toro scritti su ", f'torus_data_{major_radius}_{minor_radius}_{subdivisions_major}_{subdivisions_minor}.txt')
