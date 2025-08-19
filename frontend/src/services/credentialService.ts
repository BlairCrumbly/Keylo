import { api } from '../config/api';
import type { CredentialRequest, CredentialResponse } from '../types';

export const credentialService = {
  async getCredentials(): Promise<CredentialResponse[]> {
    const response = await api.get('/api/credentials');
    return response.data;
  },

  async saveCredential(credential: CredentialRequest): Promise<CredentialResponse> {
    const response = await api.post('/api/credentials', credential);
    return response.data;
  },

  async updateCredential(id: number, credential: CredentialRequest): Promise<CredentialResponse> {
    const response = await api.put(`/api/credentials/${id}`, credential);
    return response.data;
  },

  async deleteCredential(id: number): Promise<void> {
    await api.delete(`/api/credentials/${id}`);
  }
};
