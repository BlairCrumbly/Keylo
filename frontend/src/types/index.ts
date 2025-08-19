export interface User {
  id: number;
  email: string;
  password?: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
}


export interface JwtResponse {
  token: string;
  type: string;
  email: string;
  userId: number;
}

export interface CredentialRequest {
  id?: number;
  siteName: string;
  siteUrl?: string;
  loginEmail: string;
  loginUsername?: string;
  loginPassword: string;
  description?: string;
}

export interface CredentialResponse {
  id: number;
  siteName: string;
  siteUrl?: string;
  loginEmail: string;
  loginUsername?: string;
  loginPassword: string;
  description?: string;
  createdAt: string;
}

export interface AuthContextType {
  user: User | null;
  token: string | null;
  login: (email: string, password: string) => Promise<void>;
  register: (email: string, password: string) => Promise<void>;
  logout: () => void;
  isLoading: boolean;
}