export interface Role {
    id: number;
    name: string; // "ADMIN", "TEACHER", "STUDENT"
    isActive?: boolean;
}

export interface User {
    id: number;
    username: string;
    firstName: string;
    lastName: string;
    email: string;
    roleList: Role[];
    // Add other fields as necessary
}
