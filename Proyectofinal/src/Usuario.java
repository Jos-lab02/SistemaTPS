public class Usuario extends Persona{
        private String username;
        private String password;
        private String rol;

        // Constructor
        public Usuario(String nombre, String apellido, String edad, String username, String password, String rol) {
            super(nombre, apellido, edad);
            this.username = username;
            this.password = password;
            this.rol = rol;
        }

        // --- GETTERS Y SETTERS ---

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getRol() {
            return rol;
        }

        public void setRol(String rol) {
            this.rol = rol;
        }

        @Override
        public String toString() {
            return "Usuario{" +
                    "nombres='" + getName() + '\'' +
                    ", apellidos='" + getLastname() + '\'' +
                    ", rol='" + rol + '\'' +
                    '}';
        }
    }