import { useState, useEffect } from 'react';
import type { CredentialResponse, CredentialRequest } from '../types';
import { credentialService } from '../services/credentialService';

export const useCredentials = () => {
  const [credentials, setCredentials] = useState<CredentialResponse[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchCredentials = async () => {
    try {
      setIsLoading(true);
      setError(null);
      const data = await credentialService.getCredentials();
      setCredentials(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch credentials');
    } finally {
      setIsLoading(false);
    }
  };

  const addCredential = async (credential: CredentialRequest): Promise<void> => {
    try {
      const newCredential = await credentialService.saveCredential(credential);
      setCredentials(prev => [...prev, newCredential]);
    } catch (err) {
      throw err;
    }
  };

  const updateCredential = async (id: number, credential: CredentialRequest): Promise<void> => {
    try {
      const updatedCredential = await credentialService.updateCredential(id, credential);
      setCredentials(prev => 
        prev.map(cred => cred.id === id ? updatedCredential : cred)
      );
    } catch (err) {
      throw err;
    }
  };

  const deleteCredential = async (id: number): Promise<void> => {
    try {
      await credentialService.deleteCredential(id);
      setCredentials(prev => prev.filter(cred => cred.id !== id));
    } catch (err) {
      throw err;
    }
  };

  useEffect(() => {
    fetchCredentials();
  }, []);

  return {
    credentials,
    isLoading,
    error,
    fetchCredentials,
    addCredential,
    updateCredential,
    deleteCredential
  };
};
