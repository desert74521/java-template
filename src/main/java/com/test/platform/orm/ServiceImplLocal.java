package com.test.platform.orm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.TableInfoHelper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;

/**
 * @author sxj
 * @apiNote 重写serviceImpl类实现软删除,及更新字段赋值
 */
public class ServiceImplLocal<M extends BaseMapper<T>, T extends BaseModel> extends ServiceImpl<M, T> implements IServiceLocal<T>{

	private static String DEL_FLAG = "1";
	private static String UN_DEL_FLAG = "0";
	
	@Override
    public boolean save(T entity) {
		if(entity != null && (entity.getId() == null || "".equals(entity.getId()))) {
			entity.setCreateAt(new Date());
			entity.setUpdateAt(new Date());
			entity.setDelFlag(UN_DEL_FLAG);
		}
        return retBool(baseMapper.insert(entity));
    }
	
	@Override
	public T saveAndReturn(T entity) {
		if(entity != null && (entity.getId() == null || "".equals(entity.getId()))) {
			entity.setCreateAt(new Date());
			entity.setUpdateAt(new Date());
			entity.setDelFlag(UN_DEL_FLAG);
		}
        if(retBool(baseMapper.insert(entity))) {
        	return entity;
        } else {
        	throw new RuntimeException("save error");
        }
	}

    /**
     * 批量插入
     *
     * @param entityList ignore
     * @param batchSize ignore
     * @return ignore
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveBatch(Collection<T> entityList, int batchSize) {
        String sqlStatement = sqlStatement(SqlMethod.INSERT_ONE);
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int i = 0;
            for (T anEntityList : entityList) {
            	if(anEntityList != null && (anEntityList.getId() == null || "".equals(anEntityList.getId()))) {
            		anEntityList.setCreateAt(new Date());
            		anEntityList.setUpdateAt(new Date());
            		anEntityList.setDelFlag(UN_DEL_FLAG);
            	}
                batchSqlSession.insert(sqlStatement, anEntityList);
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
                i++;
            }
            batchSqlSession.flushStatements();
        }
        return true;
    }

    /**
     * TableId 注解存在更新记录，否插入一条记录
     *
     * @param entity 实体对象
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveOrUpdate(T entity) {
        if (null != entity) {
            Class<?> cls = entity.getClass();
            TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
            Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
            String keyProperty = tableInfo.getKeyProperty();
            Assert.notEmpty(keyProperty, "error: can not execute. because can not find column for id from entity!");
            Object idVal = ReflectionKit.getMethodValue(cls, entity, tableInfo.getKeyProperty());
            return StringUtils.checkValNull(idVal) || Objects.isNull(getById((Serializable) idVal)) ? save(entity) : updateById(entity);
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
        Assert.notEmpty(entityList, "error: entityList must not be empty");
        Class<?> cls = currentModelClass();
        TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
        Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
        String keyProperty = tableInfo.getKeyProperty();
        Assert.notEmpty(keyProperty, "error: can not execute. because can not find column for id from entity!");
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int i = 0;
            for (T entity : entityList) {
                Object idVal = ReflectionKit.getMethodValue(cls, entity, keyProperty);
                if (StringUtils.checkValNull(idVal) || Objects.isNull(getById((Serializable) idVal))) {
                    batchSqlSession.insert(sqlStatement(SqlMethod.INSERT_ONE), entity);
                } else {
                    MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
                    param.put(Constants.ENTITY, entity);
                    batchSqlSession.update(sqlStatement(SqlMethod.UPDATE_BY_ID), param);
                }
                // 不知道以后会不会有人说更新失败了还要执行插入 😂😂😂
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
                i++;
            }
            batchSqlSession.flushStatements();
        }
        return true;
    }

    @Override
    public boolean removeById(Serializable id) {
    	T obj = baseMapper.selectById(id);
    	if(obj != null && UN_DEL_FLAG.equals(obj.getDelFlag())) {
    		obj.setDelFlag(DEL_FLAG);
    		obj.setUpdateAt(new Date());
    	} else {
    		return false;
    	}
    	baseMapper.updateById(obj);
    	return true;
//        return SqlHelper.retBool(baseMapper.deleteById(id));
    }

    @Override
    public boolean removeByMap(Map<String, Object> columnMap) {
        Assert.notEmpty(columnMap, "error: columnMap must not be empty");
        columnMap.put("del_flag", UN_DEL_FLAG);
        List<T> list = baseMapper.selectByMap(columnMap);
        if(list != null && list.size() > 0) {
        	list.forEach(item -> {
        		item.setDelFlag(DEL_FLAG);
        		item.setUpdateAt(new Date());
        	});
        	this.updateBatchById(list);
        	return true;
        } else {
        	return false;
        }
//        return SqlHelper.retBool(baseMapper.deleteByMap(columnMap));
    }

    @Override
    public boolean remove(Wrapper<T> wrapper) {
    	if(wrapper == null || wrapper.isEmptyOfWhere()) {
    		wrapper = new QueryWrapper<T>().eq("del_flag", UN_DEL_FLAG);
    	} else {
    		wrapper = ((QueryWrapper<T>)wrapper).eq("del_flag", UN_DEL_FLAG);
    	}
    	List<T> list = baseMapper.selectList(wrapper);
    	if(list != null && list.size() > 0) {
        	list.forEach(item -> {
        		item.setDelFlag(DEL_FLAG);
        		item.setUpdateAt(new Date());
        	});
        	this.updateBatchById(list);
        	return true;
        } else {
        	return false;
        }
//        return SqlHelper.retBool(baseMapper.delete(wrapper));
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
    	List<T> list = baseMapper.selectBatchIds(idList);
    	if(list != null && list.size() > 0) {
        	list.forEach(item -> {
        		item.setDelFlag(DEL_FLAG);
        		item.setUpdateAt(new Date());
        	});
        	this.updateBatchById(list);
        	return true;
        } else {
        	return false;
        }
//        return SqlHelper.retBool(baseMapper.deleteBatchIds(idList));
    }

    @Override
    public boolean updateById(T entity) {
    	if(entity != null) {
    		entity.setUpdateAt(new Date());
    	}
        return retBool(baseMapper.updateById(entity));
    }
    
	@Override
	public T updateAndReturn(T entity) {
		if(entity != null) {
    		entity.setUpdateAt(new Date());
    	}
        if(retBool(baseMapper.updateById(entity))) {
        	return entity;
        } else {
        	throw new RuntimeException("update error");
        }
	}

    @Override
    public boolean update(T entity, Wrapper<T> updateWrapper) {
    	if(entity != null) {
    		entity.setUpdateAt(new Date());
    	}
        return retBool(baseMapper.update(entity, updateWrapper));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateBatchById(Collection<T> entityList, int batchSize) {
        Assert.notEmpty(entityList, "error: entityList must not be empty");
        String sqlStatement = sqlStatement(SqlMethod.UPDATE_BY_ID);
        try (SqlSession batchSqlSession = sqlSessionBatch()) {
            int i = 0;
            for (T anEntityList : entityList) {
                MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
                param.put(Constants.ENTITY, anEntityList);
                anEntityList.setUpdateAt(new Date());
                batchSqlSession.update(sqlStatement, param);
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
                i++;
            }
            batchSqlSession.flushStatements();
        }
        return true;
    }

    @Override
    public T getById(Serializable id) {
    	T model = baseMapper.selectById(id);
    	if(model != null && DEL_FLAG.equals(model.getDelFlag())) {
    		model = null;
    	}
        return model;
    }
    
    @Override
    public T getByIdContainDel(Serializable id) {
        return baseMapper.selectById(id);
    }

    @Override
    public Collection<T> listByIds(Collection<? extends Serializable> idList) {
    	List<T> list = baseMapper.selectBatchIds(idList);
    	List<T> resultList = new ArrayList<>();
    	if(list != null && list.size() > 0) {
    		list.forEach(item -> {
    			if(!DEL_FLAG.equals(item.getDelFlag())) {
    				resultList.add(item);
    			}
    		});
    	}
        return resultList;
    }

    @Override
    public Collection<T> listByMap(Map<String, Object> columnMap) {
    	if(columnMap == null) {
    		columnMap = new HashMap<>();
    	}
    	columnMap.put("del_flag", UN_DEL_FLAG);
        return baseMapper.selectByMap(columnMap);
    }

    @Override
    public T getOne(Wrapper<T> queryWrapper, boolean throwEx) {
    	if(queryWrapper == null || queryWrapper.isEmptyOfWhere()) {
    		queryWrapper = new QueryWrapper<T>().eq("del_flag", UN_DEL_FLAG);
    	} else {
    		queryWrapper = ((QueryWrapper<T>)queryWrapper).eq("del_flag", UN_DEL_FLAG);
    	}
        if (throwEx) {
            return baseMapper.selectOne(queryWrapper);
        }
        return SqlHelper.getObject(log, baseMapper.selectList(queryWrapper));
    }

    @Override
    public Map<String, Object> getMap(Wrapper<T> queryWrapper) {
    	if(queryWrapper == null || queryWrapper.isEmptyOfWhere()) {
    		queryWrapper = new QueryWrapper<T>().eq("del_flag", UN_DEL_FLAG);
    	} else {
    		queryWrapper = ((QueryWrapper<T>)queryWrapper).eq("del_flag", UN_DEL_FLAG);
    	}
        return SqlHelper.getObject(log, baseMapper.selectMaps(queryWrapper));
    }

    @Override
    public int count(Wrapper<T> queryWrapper) {
    	if(queryWrapper == null || queryWrapper.isEmptyOfWhere()) {
    		queryWrapper = new QueryWrapper<T>().eq("del_flag", UN_DEL_FLAG);
    	} else {
    		queryWrapper = ((QueryWrapper<T>)queryWrapper).eq("del_flag", UN_DEL_FLAG);
    	}
        return SqlHelper.retCount(baseMapper.selectCount(queryWrapper));
    }

    @Override
    public List<T> list(Wrapper<T> queryWrapper) {
    	if(queryWrapper == null || queryWrapper.isEmptyOfWhere()) {
    		queryWrapper = new QueryWrapper<T>().eq("del_flag", UN_DEL_FLAG);
    	} else {
    		queryWrapper = ((QueryWrapper<T>)queryWrapper).eq("del_flag", UN_DEL_FLAG);
    	}
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public IPage<T> page(IPage<T> page, Wrapper<T> queryWrapper) {
    	if(queryWrapper == null || queryWrapper.isEmptyOfWhere()) {
    		queryWrapper = new QueryWrapper<T>().eq("del_flag", UN_DEL_FLAG);
    	} else {
    		queryWrapper = ((QueryWrapper<T>)queryWrapper).eq("del_flag", UN_DEL_FLAG);
    	}
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public List<Map<String, Object>> listMaps(Wrapper<T> queryWrapper) {
    	if(queryWrapper == null || queryWrapper.isEmptyOfWhere()) {
    		queryWrapper = new QueryWrapper<T>().eq("del_flag", UN_DEL_FLAG);
    	} else {
    		queryWrapper = ((QueryWrapper<T>)queryWrapper).eq("del_flag", UN_DEL_FLAG);
    	}
        return baseMapper.selectMaps(queryWrapper);
    }

    @Override
    public <V> List<V> listObjs(Wrapper<T> queryWrapper, Function<? super Object, V> mapper) {
    	if(queryWrapper == null || queryWrapper.isEmptyOfWhere()) {
    		queryWrapper = new QueryWrapper<T>().eq("del_flag", UN_DEL_FLAG);
    	} else {
    		queryWrapper = ((QueryWrapper<T>)queryWrapper).eq("del_flag", UN_DEL_FLAG);
    	}
        return baseMapper.selectObjs(queryWrapper).stream().filter(Objects::nonNull).map(mapper).collect(Collectors.toList());
    }

    @Override
    public IPage<Map<String, Object>> pageMaps(IPage<T> page, Wrapper<T> queryWrapper) {
    	if(queryWrapper == null || queryWrapper.isEmptyOfWhere()) {
    		queryWrapper = new QueryWrapper<T>().eq("del_flag", UN_DEL_FLAG);
    	} else {
    		queryWrapper = ((QueryWrapper<T>)queryWrapper).eq("del_flag", UN_DEL_FLAG);
    	}
        return baseMapper.selectMapsPage(page, queryWrapper);
    }

    @Override
    public <V> V getObj(Wrapper<T> queryWrapper, Function<? super Object, V> mapper) {
    	if(queryWrapper == null || queryWrapper.isEmptyOfWhere()) {
    		queryWrapper = new QueryWrapper<T>().eq("del_flag", UN_DEL_FLAG);
    	} else {
    		queryWrapper = ((QueryWrapper<T>)queryWrapper).eq("del_flag", UN_DEL_FLAG);
    	}
        return SqlHelper.getObject(log, listObjs(queryWrapper, mapper));
    }


}
