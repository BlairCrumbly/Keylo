import React, { useState } from 'react';
import type { CredentialResponse } from '../../types';
import { Button } from '../common/Button';
import '../../css/credentialList.css';
import { ChevronRight } from 'lucide-react';

interface CredentialListProps {
  credentials: CredentialResponse[];
  onEdit: (credential: CredentialResponse) => void;
  onDelete: (id: number) => Promise<void>;
}

export const CredentialList: React.FC<CredentialListProps> = ({
  credentials,
  onEdit,
  onDelete
}) => {
  const [visiblePasswords, setVisiblePasswords] = useState<Set<number>>(new Set());
  const [deletingId, setDeletingId] = useState<number | null>(null);
  const [expandedId, setExpandedId] = useState<number | null>(null);

  const togglePasswordVisibility = (id: number) => {
    setVisiblePasswords(prev => {
      const newSet = new Set(prev);
      if (newSet.has(id)) {
        newSet.delete(id);
      } else {
        newSet.add(id);
      }
      return newSet;
    });
  };

  const toggleExpand = (id: number) => {
    setExpandedId(prev => (prev === id ? null : id));
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('Are you sure you want to delete this credential?')) {
      setDeletingId(id);
      try {
        await onDelete(id);
      } finally {
        setDeletingId(null);
      }
    }
  };

  if (credentials.length === 0) {
    return (
      <div className="credential-empty">
        No credentials saved yet. Add your first credential to get started!
      </div>
    );
  }

  return (
    <div className="credential-grid">
      {credentials.map((credential) => {
        const isExpanded = expandedId === credential.id;

        return (
          <div
            key={credential.id}
            className={`credential-item ${!isExpanded ? 'collapsed' : ''}`}
            onClick={() => toggleExpand(credential.id)}
          >
            <div className="credential-header">
              <h3 className="credential-title">
                {credential.siteName}
              </h3>
              <ChevronRight
                className={`arrow-icon ${isExpanded ? 'expanded' : ''}`}
              />
            </div>

            {isExpanded && (
              <div className="credential-content" onClick={(e) => e.stopPropagation()}>
                {credential.siteUrl && (
                  <div className="credential-field">
                    <span className="field-label">URL:</span>
                    <a
                      href={credential.siteUrl}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="field-link"
                    >
                      {credential.siteUrl}
                    </a>
                  </div>
                )}

                <div className="credential-field">
                  <span className="field-label">Email:</span>
                  <span className="field-value">{credential.loginEmail}</span>
                </div>

                {credential.loginUsername && (
                  <div className="credential-field">
                    <span className="field-label">Username:</span>
                    <span className="field-value">{credential.loginUsername}</span>
                  </div>
                )}

                <div className="credential-field">
                  <span className="field-label">Password:</span>
                  <div className="password-row">
                    <span className="password-value">
                      {visiblePasswords.has(credential.id)
                        ? credential.loginPassword
                        : '••••••••'}
                    </span>
                    <button
                      onClick={(e) => {
                        e.stopPropagation();
                        togglePasswordVisibility(credential.id);
                      }}
                      className="password-toggle"
                    >
                      {visiblePasswords.has(credential.id) ? 'Hide' : 'Show'}
                    </button>
                  </div>
                </div>

                {credential.description && (
                  <div className="credential-field">
                    <span className="field-label">Description:</span>
                    <p className="field-description">{credential.description}</p>
                  </div>
                )}

                <div className="credential-actions">
                  <Button
                    size="sm"
                    variant="secondary"
                    onClick={(e) => {
                      e.stopPropagation();
                      onEdit(credential);
                    }}
                  >
                    Edit
                  </Button>
                  <Button
                    size="sm"
                    variant="danger"
                    onClick={(e) => {
                      e.stopPropagation();
                      handleDelete(credential.id);
                    }}
                    isLoading={deletingId === credential.id}
                  >
                    Delete
                  </Button>
                  <div className="credential-meta">
                    Created: {new Date(credential.createdAt).toLocaleDateString()}
                  </div>
                </div>
              </div>
            )}
          </div>
        );
      })}
    </div>
  );
};
