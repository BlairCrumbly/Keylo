import { api } from '../config/api';
import type { LoginRequest, RegisterRequest, User, JwtResponse } from '../types';


export const authService = {
  async login(credentials: LoginRequest): Promise<JwtResponse> {
    const response = await api.post('/api/auth/login', credentials);
    return response.data;
  },

async register(user: RegisterRequest): Promise<User> {
  const response = await api.post('/api/auth/register', user);
  return response.data;
},


  async refreshToken(): Promise<JwtResponse> {
    const response = await api.post('/api/auth/refresh');
    return response.data;
  },

  async logout(): Promise<void> {
    await api.post('/api/auth/logout');
  },

  async deleteUser(id: number): Promise<void> {
    await api.delete(`/api/auth/${id}`);
  },

  async updateUser(user: User): Promise<User> {
    const response = await api.put('/api/auth/update', user);
    return response.data;
  },

  async getUser(id: number): Promise<User> {
    const response = await api.get(`/api/auth/${id}`);
    return response.data;
  }
};