package net.sourceforge.eclipsejetty.common;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.Path;

public class ContainerConfig
{

    private final String path;
    private final ContainerConfigType type;

    private boolean active;

    public ContainerConfig(String path, ContainerConfigType type, boolean active)
    {
        super();

        this.path = path;
        this.type = type;
        this.active = active;
    }

    public String getPath()
    {
        return path;
    }

    public ContainerConfigType getType()
    {
        return type;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public IFile getFile(IWorkspace workspace)
    {
        return getFile(workspace, type, path);
    }

    public boolean isValid(IWorkspace workspace)
    {
        try
        {
            IFile file = getFile(workspace);

            return (file == null) || file.exists();
        }
        catch (IllegalArgumentException e)
        {
            return false;
        }
    }

    public static IFile getFile(IWorkspace workspace, ContainerConfigType type, String path)
    {
        switch (type)
        {
            case DEFAULT:
                return null;

            case PATH:
                return workspace.getRoot().getFileForLocation(new Path(path));

            case WORKSPACE:
                return workspace.getRoot().getFile(new Path(path));
        }

        return null;
    }

}
