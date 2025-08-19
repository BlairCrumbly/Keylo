import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useCredentials } from '../hooks/useCredentials';
import type { CredentialResponse } from '../types';
import { Header } from '../components/layout/Header';
import { CredentialList } from '../components/credentials/CredentialList';
import { CredentialForm } from '../components/credentials/CredentialForm';
import { Modal } from '../components/common/Modal';
import { Button } from '../components/common/Button';
import '../css/dashboard.css';

export const DashboardPage: React.FC = () => {
  const navigate = useNavigate();

  const {
    credentials,
    isLoading,
    error,
    addCredential,
    updateCredential,
    deleteCredential
  } = useCredentials();

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingCredential, setEditingCredential] = useState<CredentialResponse | undefined>();

  const handleAddNew = () => {
    setEditingCredential(undefined);
    setIsModalOpen(true);
  };

  const handleEdit = (credential: CredentialResponse) => {
    setEditingCredential(credential);
    setIsModalOpen(true);
  };

  const handleSave = async (credentialData: any) => {
    if (editingCredential) {
      await updateCredential(editingCredential.id, credentialData);
    } else {
      await addCredential(credentialData);
    }
    setIsModalOpen(false);
    setEditingCredential(undefined);
  };

  const handleCancel = () => {
    setIsModalOpen(false);
    setEditingCredential(undefined);
  };

  const handleNavigateToChecker = () => {
    navigate('/check-password');
  };

  if (isLoading) {
    return (
      <div className="dashboard">
        <Header />
        <div className="dashboard-container">
          <div className="dashboard-loading">
            <div className="dashboard-loading-text">Loading credentials...</div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="dashboard">
      <Header />
      <div className="dashboard-container">
        <div className="dashboard-header">
          <h2 className="dashboard-title"></h2>
          <div className="dashboard-actions flex ">
            <Button onClick={handleAddNew} className="mr-4">Add New Credential</Button>
            <Button onClick={handleNavigateToChecker}>Check Password Strength</Button>
          </div>
        </div>

        {error && <div className="dashboard-error">Error: {error}</div>}

        <CredentialList
          credentials={credentials}
          onEdit={handleEdit}
          onDelete={deleteCredential}
        />

        <Modal
          isOpen={isModalOpen}
          onClose={handleCancel}
          title={editingCredential ? 'Edit Credential' : 'Add New Credential'}
        >
          <CredentialForm
            credential={editingCredential}
            onSave={handleSave}
            onCancel={handleCancel}
          />
        </Modal>
      </div>
    </div>
  );
};
