import React, { useState, useEffect } from 'react';
import type { CredentialRequest, CredentialResponse } from '../../types';
import { Button } from '../common/Button';
import { Input } from '../common/Input';
import '../../css/credentialForm.css';

interface CredentialFormProps {
  credential?: CredentialResponse;
  onSave: (credential: CredentialRequest) => Promise<void>;
  onCancel: () => void;
}

export const CredentialForm: React.FC<CredentialFormProps> = ({
  credential,
  onSave,
  onCancel
}) => {
  const [formData, setFormData] = useState<CredentialRequest>({
    siteName: '',
    siteUrl: '',
    loginEmail: '',
    loginUsername: '',
    loginPassword: '',
    description: ''
  });
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (credential) {
      setFormData({
        id: credential.id,
        siteName: credential.siteName,
        siteUrl: credential.siteUrl || '',
        loginEmail: credential.loginEmail,
        loginUsername: credential.loginUsername || '',
        loginPassword: credential.loginPassword,
        description: credential.description || ''
      });
    }
  }, [credential]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setIsLoading(true);

    try {
      await onSave(formData);
    } catch (err: any) {
      setError(err.response?.data || 'Failed to save credential');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <form className="credential-form" onSubmit={handleSubmit}>
      <Input
        label="Site Name*"
        name="siteName"
        value={formData.siteName}
        onChange={handleChange}
        required
        disabled={isLoading}
      />

      <Input
        label="Site URL"
        name="siteUrl"
        type="url"
        value={formData.siteUrl}
        onChange={handleChange}
        disabled={isLoading}
      />

      <Input
        label="Login Email*"
        name="loginEmail"
        type="email"
        value={formData.loginEmail}
        onChange={handleChange}
        required
        disabled={isLoading}
      />

      <Input
        label="Username"
        name="loginUsername"
        value={formData.loginUsername}
        onChange={handleChange}
        disabled={isLoading}
      />

      <Input
        label="Password*"
        name="loginPassword"
        type="password"
        value={formData.loginPassword}
        onChange={handleChange}
        required
        disabled={isLoading}
      />

      <div className="form-group">
        <label className="form-label">Description</label>
        <textarea
          name="description"
          value={formData.description}
          onChange={handleChange}
          disabled={isLoading}
          rows={3}
          className="form-textarea"
        />
      </div>

      {error && <div className="form-error">{error}</div>}

      <div className="form-actions">
        <Button
          type="button"
          variant="secondary"
          onClick={onCancel}
          disabled={isLoading}
        >
          Cancel
        </Button>
        <Button type="submit" isLoading={isLoading}>
          {credential ? 'Update' : 'Save'} Credential
        </Button>
      </div>
    </form>
  );
};
